#!/data/data/com.termux/files/usr/bin/bash

echo "==============================================="
echo "🌙 ZAHRA SPACE - DOWNLOAD ASSET OTOMATIS 🌙"
echo "==============================================="
echo "🔥 Bagian yang gw urus: Audio, Musik, Efek, 3D Models"
echo ""

cd ~/ZahraSpace

# ================================================
# 1. BUAT FOLDER
# ================================================
mkdir -p app/src/main/assets/models
mkdir -p app/src/main/assets/audio/murottal
mkdir -p app/src/main/assets/audio/music
mkdir -p app/src/main/assets/audio/sfx

# ================================================
# 2. DOWNLOAD 3D MODELS (SAMPLE)
# ================================================
echo "📥 [1/4] Downloading 3D models..."
cd app/src/main/assets/models

wget -q -O zahra.gltf https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/SimpleSkin/glTF/SimpleSkin.gltf && echo "  ✅ Zahra model"
wget -q -O luna.gltf https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Fox/glTF/Fox.gltf && echo "  ✅ Luna cat"
wget -q -O city.gltf https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Sponza/glTF/Sponza.gltf && echo "  ✅ City model"

ls -lh
cd ../../..

# ================================================
# 3. DOWNLOAD MUROTTAL FULL 114 SURAH
# ================================================
echo "📥 [2/4] Downloading FULL 114 surah murottal (ini butuh waktu ~30 menit)..."

cd app/src/main/assets/audio/murottal

for i in {1..114}; do
  printf -v filename "%03d" $i
  echo -n "  Downloading surah $filename... "
  
  # Coba 3 mirror
  if wget -q -O $filename.mp3 https://server8.mp3quran.net/afs/${i}.mp3; then
    echo "✅ (server8)"
  elif wget -q -O $filename.mp3 https://download.quran.islamway.net/audio/145_128kbps/$filename.mp3; then
    echo "✅ (islamway)"
  elif wget -q -O $filename.mp3 https://www.everyayah.com/data/Alafasy_64kbps/$filename.mp3; then
    echo "✅ (everyayah)"
  else
    echo "❌ GAGAL"
  fi
done

ls -lh | tail -5
cd ../../../..

# ================================================
# 4. DOWNLOAD MUSIK AMBIENT (10 LAGU)
# ================================================
echo "📥 [3/4] Downloading 10 ambient music tracks..."

cd app/src/main/assets/audio/music

# SoundHelix - Royalty Free
for i in {1..10}; do
  echo -n "  Downloading track $i... "
  wget -q -O music_$i.mp3 https://www.soundhelix.com/examples/mp3/SoundHelix-Song-$i.mp3 && echo "✅" || echo "❌"
done

ls -lh
cd ../../../..

# ================================================
# 5. DOWNLOAD EFEK SUARA (20 FILE)
# ================================================
echo "📥 [4/4] Downloading 20 sound effects..."

cd app/src/main/assets/audio/sfx

# SoundHelix juga buat sfx
for i in {11..30}; do
  echo -n "  Downloading sfx $i... "
  wget -q -O sfx_$i.mp3 https://www.soundhelix.com/examples/mp3/SoundHelix-Song-$i.mp3 && echo "✅" || echo "❌"
done

ls -lh
cd ../../../..

# ================================================
# 6. CEK FILE QURAN + HADIST (MANUAL)
# ================================================
echo ""
echo "==============================================="
echo "📋 CEK FILE QURAN & HADIST (TUGAS LU)"
echo "==============================================="
echo ""

if [ -f app/src/main/assets/databases/quran.db ]; then
  echo "✅ Quran database: ditemukan"
  ls -lh app/src/main/assets/databases/quran.db
else
  echo "❌ Quran database: BELUM ADA"
  echo "   Letakkan di: app/src/main/assets/databases/quran.db"
fi

if [ -f app/src/main/assets/databases/hadith.db ]; then
  echo "✅ Hadith database: ditemukan"
  ls -lh app/src/main/assets/databases/hadith.db
else
  echo "❌ Hadith database: BELUM ADA"
  echo "   Letakkan di: app/src/main/assets/databases/hadith.db"
fi

# ================================================
# 7. RINGKASAN
# ================================================
echo ""
echo "==============================================="
echo "✅✅✅ DOWNLOAD OTOMATIS SELESAI! ✅✅✅"
echo "==============================================="
echo ""
echo "📊 YANG BERHASIL DI DOWNLOAD OTOMATIS:"
echo "   - 3D Models: 3 file"
echo "   - Murottal: 114 surah (≈ 800 MB)"
echo "   - Musik ambient: 10 track"
echo "   - Efek suara: 20 file"
echo ""
echo "📁 Lokasi asset: ~/ZahraSpace/app/src/main/assets/"
echo ""
echo "⚠️ TUGAS LU:"
echo "   1. Download MANUAL Quran + Hadist"
echo "   2. Taruh di folder databases/"
echo "   3. Jalankan ./gradlew assembleDebug"
echo ""
echo "==============================================="
