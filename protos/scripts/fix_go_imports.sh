  
#!/bin/bash
declare -a dirnames=($1)

for dirname in "${dirnames[@]}"; do
    for filename in "${dirname}*pb.*"; do
        sed -i '' 's|'modeldb\ \"protos\/public'|'modeldb\ \"github\.com\/VertaAI\/modeldb-oss\/protos\/protos\/public'|g' $filename
        sed -i '' 's|'com_mitdbg_modeldb_0\ \"protos\/public'|'com_mitdbg_modeldb_0\ \"github\.com\/VertaAI\/modeldb-oss\/protos\/protos\/public'|g' $filename
        sed -i '' 's|'\"protos\/public\/modeldb\"'|'\"github\.com\/VertaAI\/modeldb-oss\/protos\/protos\/public\/modeldb\"'|g' $filename
        sed -i '' 's|'\"protos\/public\/uac\"'|'\"github\.com\/VertaAI\/modeldb-oss\/protos\/protos\/public\/uac\"'|g' $filename
        sed -i '' 's|'\"protos\/public\/common\"'|'\"github\.com\/VertaAI\/modeldb-oss\/protos\/protos\/public\/common\"'|g' $filename
    done
done
