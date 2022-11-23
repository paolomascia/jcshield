package jcshield.agent;

import com.sun.jna.* ;

public interface CryptoLibrary extends Library {
	void decrypt(Pointer in,Pointer out,int count) ;
}
