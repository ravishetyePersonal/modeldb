cd .. 
ROOT_DIR=$PWD
BACKEND_REPO='mdb-be-2'
BACKEND_BRANCH='development'
BACKEND_PROXY_REPO='Backend-Rest-Proxy'
BACKEND_PROXY_BRANCH='master'
UAC_REPO='UACService'
UAC_BRANCH='development_with_sm'
UAC_PROXY_REPO='UACService-Rest-Proxy'
UAC_PROXY_BRANCH='master'

echo cd ${BACKEND_REPO}
cd ${BACKEND_REPO}
echo git checkout origin/${BACKEND_BRANCH}
git checkout origin/${BACKEND_BRANCH}
echo ./build.sh
./build.sh
echo cd ${ROOT_DIR}
cd ${ROOT_DIR}

echo cd ${BACKEND_PROXY_REPO}
cd ${BACKEND_PROXY_REPO}
echo git checkout origin/${BACKEND_PROXY_BRANCH}
git checkout origin/${BACKEND_PROXY_BRANCH}
echo ./build.sh
./build.sh
echo cd ${ROOT_DIR}
cd ${ROOT_DIR}

echo cd ${UAC_REPO}
cd ${UAC_REPO}
echo git checkout origin/${UAC_BRANCH}
git checkout origin/${UAC_BRANCH}
echo ./build.sh
./build.sh
echo cd ${ROOT_DIR}
cd ${ROOT_DIR}

echo cd ${UAC_PROXY_REPO}
cd ${UAC_PROXY_REPO}
echo git checkout origin/${UAC_PROXY_BRANCH}
git checkout origin/${UAC_PROXY_BRANCH}
echo ./build.sh
./build.sh
echo cd ${ROOT_DIR}
cd ${ROOT_DIR}

echo cd ${BACKEND_REPO}
cd ${BACKEND_REPO}
echo docker-compose -f docker-compose-all.yaml up
docker-compose -f docker-compose-all.yaml up
