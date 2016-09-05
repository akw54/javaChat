/*
 * Adam Wolf
 * CS283
 * Assignment 3 - Chat Program
 */
import java.math.BigInteger;
import java.util.Random;

public class MiniRSAImpl implements MiniRSA {
	private Long e, c, d;
	
	public MiniRSAImpl() {}
	public MiniRSAImpl(long e, long c, long d) {
		this.e = e;
		this.c = c;
		this.d = d;
	}
	
	@Override
	public long getCoprime(long val) {
		long coprime = new Random().nextInt(Integer.MAX_VALUE), t;
		while ((t = getGCD(coprime, val)) > 1) { coprime /= t; }
		return coprime;
	}

	@Override
	public long endecrypt(long msg_or_cipher, long key, long c) {
		return getModulo(msg_or_cipher, key, c);
	}

	@Override
	public long getGCD(long a, long b) {
		return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).longValue();
	}

	@Override
	public long getModInverse(long base, long m) {
		return BigInteger.valueOf(base).modInverse(BigInteger.valueOf(m)).longValue();
	}

	@Override
	public long getModulo(long a, long b, long c) {
		return BigInteger.valueOf(a).modPow(BigInteger.valueOf(b), BigInteger.valueOf(c)).longValue();
	}
	
	public long getPrime(int nthPrime) {
		BigInteger prime = BigInteger.ZERO;
		for (int i = 0; i < nthPrime; i++) {
			prime = prime.nextProbablePrime();
		}
		return prime.longValue();
	}
	
	@Override
	public long getTotient(long n) {
		long totient = 1;
		for (int i = 2; i < n; i++) {
			if (getGCD(i, n) == 1) {
				totient++;
			}
		}
		return totient;
	}

	/*
	 * Encrypts a message given a string and returns a long array 
	 * consisting of the encrypted characters.
	 */
	public long[] encrypt(String message) {
		if (e != null && c != null) {
			char[] chars = message.toCharArray();
			long[] encryptedMsg = new long[chars.length];
			for (int i = 0; i < chars.length; i++) {
				encryptedMsg[i] = endecrypt((int)chars[i], e, c);
			}
			return encryptedMsg;
		}
		return null;
	}
	
	/*
	 * Decrypts a message given an encrypted message, assuming the keys are correct.
	 */
	public String decrypt(long[] encryptedMsg) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < encryptedMsg.length; i++) {
			b.append((char) getModulo(encryptedMsg[i], d, c));
		}
		return b.toString();
	}
	
	public void crackRSA() {
		System.out.println("lol i tried");
	}
	
}
