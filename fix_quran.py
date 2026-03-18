import re

with open('quran-indonesia.sql', 'r', encoding='utf-8') as f:
    content = f.read()

# Ganti "..." jadi '...' untuk string
content = re.sub(r'"([^"]*)"', r"'\1'", content)

with open('quran-indonesia_fixed.sql', 'w', encoding='utf-8') as f:
    f.write(content)

print("✅ Fixed SQL disimpan sebagai quran-indonesia_fixed.sql")
