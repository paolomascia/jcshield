gcc -lstdc++ -std=c++11 -fPIC  -c  AES.cpp
gcc -shared -o libcrypto.so crypto.o AES.o

