echo "# Contributors\n" > ../CONTRIBUTORS.md
echo "Contributors in alphabetic order of name." >> ../CONTRIBUTORS.md
git shortlog --summary  --email | cut -f 2 >> ../CONTRIBUTORS.md
