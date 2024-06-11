package com.eul4.common.wrapper;

public class Pitch
{
	public static final float Gb0 = 0.500F;
	public static final float G0 = 0.530F;
	public static final float Ab0 = 0.561F;
	public static final float A0 = 0.595F;
	public static final float Bb0 = 0.630F;
	public static final float B0 = 0.667F;
	public static final float C1 = 0.707F;
	public static final float Db1 = 0.749F;
	public static final float D1 = 0.794F;
	public static final float Eb1 = 0.841F;
	public static final float E1 = 0.891F;
	public static final float F1 = 0.944F;
	public static final float Gb1 = 1.000F;
	public static final float G1 = 1.059F;
	public static final float Ab1 = 1.122F;
	public static final float A1 = 1.189F;
	public static final float Bb1 = 1.260F;
	public static final float B1 = 1.335F;
	public static final float C2 = 1.414F;
	public static final float Db2 = 1.498F;
	public static final float D2 = 1.587F;
	public static final float Eb2 = 1.682F;
	public static final float E2 = 1.782F;
	public static final float F2 = 1.888F;
	public static final float Gb2 = 2.000F;
	
	public static float getPitch(int note)
	{
		return switch(note)
		{
			case 0 -> Gb0;
			case 1 -> G0;
			case 2 -> Ab0;
			case 3 -> A0;
			case 4 -> Bb0;
			case 5 -> B0;
			case 6 -> C1;
			case 7 -> Db1;
			case 8 -> D1;
			case 9 -> Eb1;
			case 10 -> E1;
			case 11 -> F1;
			case 12 -> Gb1;
			case 13 -> G1;
			case 14 -> Ab1;
			case 15 -> A1;
			case 16 -> Bb1;
			case 17 -> B1;
			case 18 -> C2;
			case 19 -> Db2;
			case 20 -> D2;
			case 21 -> Eb2;
			case 22 -> E2;
			case 23 -> F2;
			default -> Gb2;
		};
	}
	
	public static float max()
	{
		return Gb2;
	}
	
	public static float min()
	{
		return Gb0;
	}
}
