import sqlite3
import os
import re

print("📦 Membangun database Hadits...")

db_path = 'app/src/main/assets/databases/hadith.db'
conn = sqlite3.connect(db_path)
c = conn.cursor()

# Buat tabel utama
c.execute('''
CREATE TABLE IF NOT EXISTS hadits (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    kitab TEXT,
    nomor TEXT,
    arab TEXT,
    terjemah TEXT,
    derajat TEXT
)
''')
c.execute("DELETE FROM hadits")

def parse_sql_inserts(content):
    """Ekstrak nilai dari INSERT INTO statements"""
    inserts = []
    
    # Cari pola INSERT INTO ...
    pattern = r"INSERT INTO\s+(\w+)\s*\((.*?)\)\s*VALUES\s*\((.*?)\);"
    matches = re.findall(pattern, content, re.DOTALL | re.IGNORECASE)
    
    for match in matches:
        table_name = match[0]
        columns = match[1].split(',')
        values_str = match[2]
        
        # Parse values (sederhana, asumsi string dipisah koma)
        values = []
        current = ""
        in_string = False
        for char in values_str:
            if char == "'" and not in_string:
                in_string = True
                current += char
            elif char == "'" and in_string:
                in_string = False
                current += char
            elif char == ',' and not in_string:
                values.append(current.strip())
                current = ""
            else:
                current += char
        if current:
            values.append(current.strip())
        
        inserts.append((table_name, columns, values))
    
    return inserts

# Fungsi untuk import file SQL
def import_sql_file(file_path, kitab_name):
    print(f"  📖 Membaca {kitab_name}...")
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
        
        # Coba parsing INSERT
        inserts = parse_sql_inserts(content)
        
        count = 0
        for table_name, columns, values in inserts:
            try:
                # Asumsikan kolom: id, arab, terjemah, dll
                if len(values) >= 3:
                    arab = values[1].strip("'\"") if len(values) > 1 else ""
                    terjemah = values[2].strip("'\"") if len(values) > 2 else ""
                    
                    c.execute(
                        "INSERT INTO hadits (kitab, arab, terjemah) VALUES (?, ?, ?)",
                        (kitab_name, arab, terjemah)
                    )
                    count += 1
            except Exception as e:
                continue
        
        print(f"    ✅ {count} hadits")

# Daftar file hadits
hadits_files = [
    ('shahih-bukhari.sql', 'Shahih Bukhari'),
    ('shahih-muslim.sql', 'Shahih Muslim'),
    ('sunan-abu-daud.sql', 'Sunan Abu Daud'),
    ('sunan-tirmidzi.sql', 'Sunan Tirmidzi'),
    ('sunan-nasai.sql', 'Sunan Nasai'),
    ('sunan-ibnu-majah.sql', 'Sunan Ibnu Majah'),
    ('musnad-ahmad.sql', 'Musnad Ahmad'),
    ('musnad-syafii.sql', 'Musnad Syafii'),
    ('riyadhus-shalihin.sql', 'Riyadhus Shalihin'),
    ('riyadhus-shalihin-arab.sql', 'Riyadhus Shalihin (Arab)'),
    ('muwatho_malik.sql', 'Muwatho Malik'),
    ('musnad_darimi.sql', 'Musnad Darimi')
]

# Import semua file
for filename, kitab in hadits_files:
    file_path = f'app/src/main/assets/databases/hadits_sql/{filename}'
    if os.path.exists(file_path):
        import_sql_file(file_path, kitab)
    else:
        print(f"  ⚠️ File {filename} tidak ditemukan")

conn.commit()

# Cek hasil
c.execute("SELECT COUNT(*) FROM hadits")
total = c.fetchone()[0]
print(f"\n✅ Total hadits: {total}")

if total > 0:
    print("\n📊 Sample 5 hadits pertama:")
    for row in c.execute("SELECT kitab, arab, terjemah FROM hadits LIMIT 5"):
        arab_preview = row[1][:50] + "..." if row[1] and len(row[1]) > 50 else (row[1] or "-")
        terjemah_preview = row[2][:50] + "..." if row[2] and len(row[2]) > 50 else (row[2] or "-")
        print(f"  [{row[0]}] {arab_preview} - {terjemah_preview}")

conn.close()
