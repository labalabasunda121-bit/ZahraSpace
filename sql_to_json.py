import json
import os
import re

print("📦 Konversi SQL ke JSON (Versi Lengkap)...")

sql_dir = 'app/src/main/assets/databases/hadits_sql/'
output_dir = 'app/src/main/assets/hadits/'

os.makedirs(output_dir, exist_ok=True)

# Daftar file
sql_files = [
    'shahih-bukhari.sql',
    'shahih-muslim.sql',
    'sunan-abu-daud.sql',
    'sunan-tirmidzi.sql',
    'sunan-nasai.sql',
    'sunan-ibnu-majah.sql',
    'musnad-ahmad.sql',
    'musnad-syafii.sql',
    'riyadhus-shalihin.sql',
    'riyadhus-shalihin-arab.sql',
    'muwatho_malik.sql',
    'musnad_darimi.sql'
]

for sql_file in sql_files:
    file_path = sql_dir + sql_file
    if os.path.exists(file_path):
        print(f"📖 Membaca {sql_file}...")
        
        with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
            content = f.read()
        
        # Cari semua INSERT INTO
        pattern = r"INSERT INTO .*? VALUES \((.*?)\);"
        matches = re.findall(pattern, content, re.DOTALL)
        
        hadits_list = []
        for match in matches:
            try:
                # Split values (sederhana)
                values = match.split(',')
                if len(values) >= 3:
                    # Bersihin kutip
                    arab = values[1].strip("'\" \n\t\r")
                    terjemah = values[2].strip("'\" \n\t\r")
                    
                    hadits_list.append({
                        'arab': arab,
                        'terjemah': terjemah
                    })
            except:
                continue
        
        # Simpan JSON
        json_file = sql_file.replace('.sql', '.json')
        with open(output_dir + json_file, 'w', encoding='utf-8') as f:
            json.dump(hadits_list, f, indent=2, ensure_ascii=False)
        
        print(f"  ✅ {len(hadits_list)} hadits -> {json_file}")

print("\n✅ Selesai!")
