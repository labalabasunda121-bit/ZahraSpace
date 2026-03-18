#!/data/data/com.termux/files/usr/bin/bash

cd ~/ZahraSpace/app/src/main/assets/databases

# Buat database SQLite baru
sqlite3 hadith.db << 'EOF'
DROP TABLE IF EXISTS hadist;
CREATE TABLE hadist (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  kitab TEXT,
  nomor TEXT,
  arab TEXT,
  terjemah TEXT,
  derajat TEXT
);
EOF

# Fungsi untuk import file SQL
import_sql() {
  local file=$1
  local kitab=$2
  echo "📥 Converting $kitab..."
  
  # Baca file SQL dan ekstrak INSERT statements
  grep -i "INSERT INTO" "$file" | while read line; do
    # Bersihin karakter aneh dan escape quotes
    cleaned=$(echo "$line" | sed "s/INSERT INTO .* VALUES (//g" | sed "s/);$//g" | sed "s/'/''/g")
    
    # Insert ke SQLite (contoh sederhana - perlu disesuaikan dengan struktur aslinya)
    sqlite3 hadith.db "INSERT INTO hadist (kitab, arab) VALUES ('$kitab', '$cleaned');" 2>/dev/null
  done
}

# Import semua file
cd hadits_sql

import_sql "shahih-bukhari.sql" "Shahih Bukhari"
import_sql "shahih-muslim.sql" "Shahih Muslim"
import_sql "sunan-abu-daud.sql" "Sunan Abu Daud"
import_sql "sunan-tirmidzi.sql" "Sunan Tirmidzi"
import_sql "sunan-nasai.sql" "Sunan Nasa'i"
import_sql "sunan-ibnu-majah.sql" "Sunan Ibnu Majah"
import_sql "musnad-ahmad.sql" "Musnad Ahmad"
import_sql "musnad-syafii.sql" "Musnad Syafi'i"
import_sql "riyadhus-shalihin.sql" "Riyadhus Shalihin"
import_sql "riyadhus-shalihin-arab.sql" "Riyadhus Shalihin (Arab)"
import_sql "muwatho_malik.sql" "Muwatho' Malik"
import_sql "musnad_darimi.sql" "Musnad Darimi"

cd ../../..

echo ""
echo "✅ Konversi selesai!"
ls -lh app/src/main/assets/databases/hadith.db
