# note this is hacky; this script creates a k8s secret using config.yaml
import os

SECRET_NAME = "modeldb-backend-config-secret"
INPUT_CONFIG = "config.yaml"
OUTPUT_CONFIG = "config.k8s.yaml"
SPACES_TO_TABS=2
SPACE=' '

with open(INPUT_CONFIG, 'r') as f:
    inner_config = (f.readlines())

with open(OUTPUT_CONFIG, 'w') as f:
    s = (
            "apiVersion: v1\n"
            "kind: Secret\n", 
            "metadata:\n",
            "  name: {}\n".format(SECRET_NAME),
            "type: Opaque\n",
            "stringData:\n",
            "  config.yaml: |-\n"
        ) 
    f.write(''.join(s))
    for line in inner_config:
        f.write(''.join([SPACES_TO_TABS * SPACE * 2]) + line)

# encrypt using sops
os.system("sh encrypt-config.sh")
