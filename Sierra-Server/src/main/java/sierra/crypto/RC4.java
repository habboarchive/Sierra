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

public class RC4
{
    private int i = 0;
    private int j = 0;
    private int[] Table;

    public RC4()
    {
        this.Table = new int[256];
    }

    public RC4(byte[] key)
    {
        this.Table = new int[256];

        this.Init(key);
    }

    public void Init(byte[] key)
    {
        int k = key.length;
        this.i = 0;
        while (this.i < 256)
        {
            this.Table[this.i] = this.i;
            this.i++;
        }

        this.i = 0;
        this.j = 0;
        while (this.i < 0x0100)
        {
            this.j = (((this.j + this.Table[this.i]) + key[(this.i % k)]) % 256);
            this.Swap(this.i, this.j);
            this.i++;
        }

        this.i = 0;
        this.j = 0;
    }

    public void Swap(int a, int b)
    {
        int k = this.Table[a];
        this.Table[a] = this.Table[b];
        this.Table[b] = k;
    }

    public byte[] Decipher(byte[] bytes)
    {
        int k = 0;
        byte[] result = new byte[bytes.length];
        int pos = 0;

        for (int a = 0; a < bytes.length; a++)
        {
            this.i = ((this.i + 1) % 256);
            this.j = ((this.j + this.Table[this.i]) % 256);
            this.Swap(this.i, this.j);
            k = ((this.Table[this.i] + this.Table[this.j]) % 256);
            result[pos++] = (byte)(bytes[a] ^ this.Table[k]);
        }

        return result;
    }

}