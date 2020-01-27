  
#!/bin/bash
declare -a dirnames=($1)

for dirname in "${dirnames[@]}"; do
    for filename in "${dirname}*pb.*"; do
        sed -i '' 's|'modeldb\ \"protos\/public'|'modeldb\ \"github\.com\/VertaAI\/protos-all-go\/protos\/public'|g' $filename
        sed -i '' 's|'uac\ \"protos\/private'|'uac\ \"github\.com\/VertaAI\/protos-all-go\/protos\/private'|g' $filename
        sed -i '' 's|'com_mitdbg_modeldb_0\ \"protos\/public'|'com_mitdbg_modeldb_0\ \"github\.com\/VertaAI\/protos-all-go\/protos\/public'|g' $filename
        sed -i '' 's|'\"protos\/public\/modeldb\"'|'\"github\.com\/VertaAI\/protos-all-go\/protos\/public\/modeldb\"'|g' $filename
        sed -i '' 's|'\"protos\/public\/uac\"'|'\"github\.com\/VertaAI\/protos-all-go\/protos\/public\/uac\"'|g' $filename
        sed -i '' 's|'\"protos\/public\/common\"'|'\"github\.com\/VertaAI\/protos-all-go\/protos\/public\/common\"'|g' $filename
    done
done