package jcshield;

import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	public static void encryptFile(byte[] key,String file)
	{
		SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

		Cipher cipher;
		try 
		{
			Path path = Paths.get(file);
			byte[] data = Files.readAllBytes(path);
			
			cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			byte[] encryptedData = cipher.doFinal(data);
			
			Files.delete(path);
			Files.write(path,encryptedData);
					
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}
}
