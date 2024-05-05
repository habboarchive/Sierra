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
import java.util.Random;

public class DiffieHellman
{
	public int BITLENGTH = 30;

	public BigInteger Prime;
	public BigInteger Generator;

	public BigInteger PrivateKey;
	public BigInteger PublicKey;

	public BigInteger PublicClientKey;

	public BigInteger SharedKey;

	public DiffieHellman()
	{
		this.InitDH();
	}

	public DiffieHellman(int b)
	{
		this.BITLENGTH = b;

		this.InitDH();
	}

	private void InitDH()
	{
		this.PublicKey = BigInteger.valueOf(0);
		if (this.PublicKey.intValue() == 0)
		{
			this.Prime = BigInteger.probablePrime(BITLENGTH, new Random());
			this.Generator = BigInteger.probablePrime(BITLENGTH, new Random());

			this.PrivateKey = new BigInteger(GenerateRandomHexString(BITLENGTH), 16);

			if (this.Generator.intValue() > this.Prime.intValue())
			{
				BigInteger temp = this.Prime;
				this.Prime = this.Generator;
				this.Generator = temp;
			}

			this.PublicKey = this.Generator.modPow(this.PrivateKey, this.Prime);
		}
	}

	public DiffieHellman(BigInteger prime, BigInteger generator)
	{
		this.Prime = prime;
		this.Generator = generator;

		this.PrivateKey = new BigInteger(GenerateRandomHexString(BITLENGTH), 16);

		if (this.Generator.intValue() > this.Prime.intValue())
		{
			BigInteger temp = this.Prime;
			this.Prime = this.Generator;
			this.Generator = temp;
		}

		this.PublicKey = this.Generator.modPow(this.PrivateKey, this.Prime);
	}

	public void GenerateSharedKey(String ckey)
	{
		try
		{
			this.PublicClientKey = new BigInteger(ckey);

			this.SharedKey = this.PublicClientKey.modPow(this.PrivateKey, this.Prime);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String GenerateRandomHexString(int len)
	{
		int rand = 0;
		String result = "";

		Random rnd = new Random();

		for (int i = 0; i < len; i++)
		{
			rand =  1 + (int)(rnd.nextDouble() * 254); // 1 - 255
			result += Integer.toString(rand, 16);
		}
		return result;
	}
}