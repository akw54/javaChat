/*
 * Adam Wolf
 * CS283
 * Assignment 3 - Chat Program
 */
public class GenerateKeys {
	public static void main(String[] args) {
		if (args.length == 2) {
			MiniRSAImpl rsa = new MiniRSAImpl();
			long a = rsa.getPrime(Integer.parseInt(args[0]));
			long b = rsa.getPrime(Integer.parseInt(args[1]));
			long c = a * b;
			long m = (a - 1) * (b - 1);
			long e = rsa.getCoprime(Double.valueOf(Math.pow(m, 2)).longValue());
			long d = rsa.getModInverse(e, m);
			System.out.printf("Public Key: (e: %d, c: %d)\nPrivate Key: (d: %d, c: %d)\n", e, c, d, c);
		} else {
			System.out.println("ERROR: Invalid number of arguments.");
		}
	}
}
