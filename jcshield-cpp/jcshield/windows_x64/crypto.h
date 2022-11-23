#ifndef INDLL_H
#define INDLL_H

#ifdef EXPORTING_DLL
    extern "C" __declspec(dllexport) void decrypt(unsigned char* in,unsigned char* out,int count); 
#else
    extern "C" __declspec(dllimport) void decrypt(unsigned char* in,unsigned char* out,int count);
#endif

#endif