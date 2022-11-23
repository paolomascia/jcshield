#define EXPORTING_DLL

#include "crypto.h"
#include "AES/AES.h"

const unsigned int BLOCK_BYTES_LENGTH = 16 * sizeof(unsigned char);


void decrypt(unsigned char *in, unsigned char *out,int count)
{
    // Dummy Key
    unsigned char key[] = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};

    AES aes(AESKeyLength::AES_128);

    unsigned char *outpntr = out ;
    unsigned char *inpntr  = in ;

    int numblocks = count / 16 ;
    
    for(int j=0;j<numblocks;j++)
    {
        unsigned char *buffer = aes.DecryptECB(inpntr, BLOCK_BYTES_LENGTH, key);
        memcpy((void*)outpntr,(void*)buffer,BLOCK_BYTES_LENGTH);
        delete[] buffer ;

        inpntr  += 16 ;
        outpntr += 16 ;
    }
}
