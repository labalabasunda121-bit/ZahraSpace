import json
import sqlite3
import os
import glob
import re

print("📦 Membangun database Quran...")

conn = sqlite3.connect('app/src/main/assets/databases/quran.db')
c = conn.cursor()

c.execute('''
CREATE TABLE IF NOT EXISTS quran (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    surah INTEGER,
    ayat INTEGER,
    arab TEXT,
    indo TEXT
)
''')
c.execute("DELETE FROM quran")

print("📖 Membaca teks Arab dari folder surah/...")
surah_dir = 'temp_quran/source/surah/'

for surah_file in sorted(glob.glob(surah_dir + '*.json')):
    surah_num = int(re.search(r'\d+', os.path.basename(surah_file)).group())
    
    with open(surah_file, 'r', encoding='utf-8') as f:
        data = json.load(f)
        
        # Ambil objek verse
        verses = data.get('verse', {})
        for key, text in verses.items():
            if key.startswith('verse_'):
                ayat_num = int(key.replace('verse_', ''))
                c.execute(
                    "INSERT INTO quran (surah, ayat, arab) VALUES (?, ?, ?)",
                    (surah_num, ayat_num, text)
                )
    print(f"  ✅ Surah {surah_num}")

print("📖 Membaca terjemahan Indonesia...")
trans_dir = 'temp_quran/source/translation/id/'

for trans_file in sorted(glob.glob(trans_dir + '*.json')):
    surah_num = int(re.search(r'\d+', os.path.basename(trans_file)).group())
    
    with open(trans_file, 'r', encoding='utf-8') as f:
        data = json.load(f)
        
        verses = data.get('verse', {})
        for key, text in verses.items():
            if key.startswith('verse_'):
                ayat_num = int(key.replace('verse_', ''))
                c.execute(
                    "UPDATE quran SET indo = ? WHERE surah = ? AND ayat = ?",
                    (text, surah_num, ayat_num)
                )
    print(f"  ✅ Terjemahan Surah {surah_num}")

conn.commit()

c.execute("SELECT COUNT(*) FROM quran")
total = c.fetchone()[0]
print(f"\n✅ Total ayat: {total}")

if total > 0:
    print("\n📊 Sample:")
    for row in c.execute("SELECT surah, ayat, arab, indo FROM quran LIMIT 5"):
        print(f"  {row[0]}:{row[1]} - {row[2][:30]}... - {row[3][:30]}...")

conn.close()
