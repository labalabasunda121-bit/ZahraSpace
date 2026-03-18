import os
import subprocess

print("📦 FINAL IMPORT V3 - Hadits ke SQLite...")

db_path = 'app/src/main/assets/databases/hadith.db'
sql_dir = 'app/src/main/assets/databases/hadits_sql/'

if os.path.exists(db_path):
    os.remove(db_path)

# ✅ PAKAI FILE _CLEAN3 (BUKAN _CLEAN2)
clean_files = [
    'shahih-bukhari_clean3.sql',
    'shahih-muslim_clean3.sql',
    'sunan-abu-daud_clean3.sql',
    'sunan-tirmidzi_clean3.sql',
    'sunan-nasai_clean3.sql',
    'sunan-ibnu-majah_clean3.sql',
    'musnad-ahmad_clean3.sql',
    'musnad-syafii_clean3.sql',
    'riyadhus-shalihin_clean3.sql',
    'riyadhus-shalihin-arab_clean3.sql',
    'muwatho_malik_clean3.sql',
    'musnad_darimi_clean3.sql'
]

for clean_file in clean_files:
    file_path = sql_dir + clean_file
    if os.path.exists(file_path):
        print(f"📥 Importing {clean_file}...")
        cmd = f"sqlite3 {db_path} < {file_path}"
        result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
        if result.stderr:
            print(f"  ⚠️ Error: {result.stderr[:100]}...")
        else:
            print(f"  ✅ OK")
    else:
        print(f"  ⚠️ File {clean_file} tidak ditemukan")

print("\n✅ Import selesai!")

if os.path.exists(db_path):
    size = os.path.getsize(db_path) / (1024*1024)
    print(f"📊 Ukuran database: {size:.2f} MB")
    cmd = f"sqlite3 {db_path} '.tables'"
    result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
    print(f"📋 Tabel: {result.stdout}")
