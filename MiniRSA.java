/*
 * Adam Wolf
 * CS283
 * Assignment 3 - Chat Program
 */
public interface MiniRSA {

	public long getCoprime(long val);
	
	public long endecrypt(long msg_or_cipher, long key, long c);
	
	public long getGCD(long a, long b);
	
	public long getModInverse(long base, long m);
	
	public long getModulo(long a, long b, long c);
	
	public long getTotient(long n);
	
}
