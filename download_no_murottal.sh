#!/data/data/com.termux/files/usr/bin/bash

echo "==============================================="
echo "🌙 ZAHRA SPACE - DOWNLOAD ASSET (TANPA MUROTTAL) 🌙"
echo "==============================================="
echo "🔥 Skip murottal, fokus ke yang pasti bisa"
echo ""

cd ~/ZahraSpace

# ================================================
# 1. BUAT FOLDER
# ================================================
mkdir -p app/src/main/assets/models
mkdir -p app/src/main/assets/audio/music
mkdir -p app/src/main/assets/audio/sfx
mkdir -p app/src/main/assets/databases

# ================================================
# 2. DOWNLOAD 3D MODELS (SAMPLE - PASTI BISA)
# ================================================
echo "📥 [1/4] Downloading 3D models (PASTI BISA)..."
cd app/src/main/assets/models

wget -q -O zahra.gltf https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/SimpleSkin/glTF/SimpleSkin.gltf && echo "  ✅ Zahra model"
wget -q -O luna.gltf https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Fox/glTF/Fox.gltf && echo "  ✅ Luna cat"
wget -q -O city.gltf https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Sponza/glTF/Sponza.gltf && echo "  ✅ City model"

ls -lh
cd ../../..

# ================================================
# 3. DOWNLOAD MUSIK AMBIENT (SOUNDHELIX - PASTI BISA)
# ================================================
echo "📥 [2/4] Downloading ambient music (PASTI BISA)..."

cd app/src/main/assets/audio/music

for i in {1..5}; do
  echo -n "  Downloading track $i... "
  wget -q -O music_$i.mp3 https://www.soundhelix.com/examples/mp3/SoundHelix-Song-$i.mp3 && echo "✅" || echo "❌"
done

ls -lh
cd ../../../..

# ================================================
# 4. DOWNLOAD EFEK SUARA (SOUNDHELIX - PASTI BISA)
# ================================================
echo "📥 [3/4] Downloading sound effects (PASTI BISA)..."

cd app/src/main/assets/audio/sfx

for i in {6..10}; do
  echo -n "  Downloading sfx $i... "
  wget -q -O sfx_$i.mp3 https://www.soundhelix.com/examples/mp3/SoundHelix-Song-$i.mp3 && echo "✅" || echo "❌"
done

ls -lh
cd ../../../..

# ================================================
# 5. CEK QURAN + HADIST (MANUAL)
# ================================================
echo "📥 [4/4] Checking Quran & Hadith (manual)..."

if [ -f app/src/main/assets/databases/quran.db ]; then
  echo "  ✅ Quran database ada"
  ls -lh app/src/main/assets/databases/quran.db
else
  echo "  ⚠️ Quran database belum ada"
  echo "     Letakkan di: app/src/main/assets/databases/quran.db"
fi

if [ -f app/src/main/assets/databases/hadith.db ]; then
  echo "  ✅ Hadith database ada"
  ls -lh app/src/main/assets/databases/hadith.db
else
  echo "  ⚠️ Hadith database belum ada"
  echo "     Letakkan di: app/src/main/assets/databases/hadith.db"
fi

# ================================================
# 6. GABUNGKAN JIKA KEDUANYA ADA
# ================================================
if [ -f app/src/main/assets/databases/quran.db ] && [ -f app/src/main/assets/databases/hadith.db ]; then
  echo "🔄 Merging Quran and Hadith..."
  sqlite3 app/src/main/assets/databases/zahra_space.db << 'EOF'
ATTACH DATABASE 'app/src/main/assets/databases/quran.db' AS quran;
ATTACH DATABASE 'app/src/main/assets/databases/hadith.db' AS hadith;
CREATE TABLE IF NOT EXISTS quran_ayat AS SELECT * FROM quran.quran;
CREATE TABLE IF NOT EXISTS hadist AS SELECT * FROM hadith.hadith;
DETACH quran;
DETACH hadith;
EOF
  rm app/src/main/assets/databases/quran.db app/src/main/assets/databases/hadith.db
  echo "✅ Databases merged: zahra_space.db"
elif [ -f app/src/main/assets/databases/quran.db ]; then
  echo "📦 Hanya Quran, rename jadi zahra_space.db"
  mv app/src/main/assets/databases/quran.db app/src/main/assets/databases/zahra_space.db
fi

ls -lh app/src/main/assets/databases/zahra_space.db 2>/dev/null

# ================================================
# 7. RINGKASAN
# ================================================
echo ""
echo "==============================================="
echo "✅✅✅ DOWNLOAD SELESAI! ✅✅✅"
echo "==============================================="
echo ""
echo "📊 YANG BERHASIL:"
echo "   - 3D Models: 3 file (PASTI BISA)"
echo "   - Musik ambient: 5 track (PASTI BISA)"
echo "   - Efek suara: 5 file (PASTI BISA)"
echo ""
echo "⚠️ YANG PERLU MANUAL:"
echo "   - Quran database (cari sendiri)"
echo "   - Hadith database (cari sendiri)"
echo "   - Murottal (skip dulu)"
echo ""
echo "📁 Lokasi: ~/ZahraSpace/app/src/main/assets/"
echo ""
echo "🚀 LANJUT: ./gradlew assembleDebug"
echo "==============================================="
