package jcshield.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class CryptoAgent
{
	public static void premain(String agentArgs, Instrumentation inst)
	{
		System.out.println("Starting JAVA Agent.....");
		
		inst.addTransformer(new ClassFileTransformer()
		{

			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] buffer) throws IllegalClassFormatException
			{
				
				if ( (buffer[0]==-54) && (buffer[1]==-2) && (buffer[2]==-70) && (buffer[3]==-66))
				{
					return buffer ;
				}
				
				System.out.println("Decrypting " +  className + " ....");
				
				try
				{
					CryptoLibrary lib = (CryptoLibrary) Native.loadLibrary("crypto", CryptoLibrary.class);
					
					Pointer ptrin = new Memory(buffer.length);
					ptrin.write(0, buffer, 0, buffer.length);
					
					lib.decrypt(ptrin, ptrin, buffer.length);
	
					byte[] decrytedData = ptrin.getByteArray(0, buffer.length);
					
					if ( (decrytedData[0]==-54) && (decrytedData[1]==-2) && (decrytedData[2]==-70) && (decrytedData[3]==-66))
					{
						System.out.println( className + " decrypted!");
					}
					
					int padding = decrytedData[decrytedData.length -1] ;
					
					byte[] depaddedData = new byte[decrytedData.length - padding];
					
					System.arraycopy(decrytedData, 0, depaddedData, 0, decrytedData.length - padding);
					
					return depaddedData;
				}
				catch(Exception ex)
				{	
					throw new IllegalClassFormatException() ;
					
				}
			}
		});
		
		System.out.println("JAVA Agent Started");
		
	}
}