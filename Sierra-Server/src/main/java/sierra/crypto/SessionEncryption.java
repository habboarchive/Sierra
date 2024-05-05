/*
 * Copyright (c) 2012 Quackster <alex.daniel.97@gmail>. 
 * 
 * This file is part of Sierra.
 * 
 * Sierra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sierra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sierra.  If not, see <http ://www.gnu.org/licenses/>.
 */

package sierra.crypto;

import java.math.BigInteger;

public class SessionEncryption
{
	public Boolean Initialized;
	
	private RC4 RC4;
	private RSA RSA;
	private DiffieHellman diffeHellman;

	public SessionEncryption(BigInteger n, BigInteger e, BigInteger d)
	{
		this.RSA = new RSA(n, e, d, BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(0));

		this.RC4 = new RC4();
		this.diffeHellman = new DiffieHellman(200);
		this.Initialized = false;
	}

	public Boolean InitializeRC4(String ctext)
	{
		try
		{
			String publickey = RSA.Decrypt(ctext);

			this.diffeHellman.GenerateSharedKey(publickey);

			this.RC4.Init(this.diffeHellman.SharedKey.toString().getBytes());

			this.Initialized = true;

			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public DiffieHellman getDiffieHellman() {
		return diffeHellman;
	}

	public String getSharedKey() {
		return diffeHellman.SharedKey.toString();
	}

	public String getPublicKey() {
		return diffeHellman.PublicKey.toString();
	}

	public String getPrivateKey() {
		return diffeHellman.PrivateKey.toString();
	}

	public RC4 getRC4() {
		return RC4;
	}
}
