package org.jointheleague.graphical.robot;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.JOptionPane;

public class Util {

	public static int clamp(int val, int lo, int hi)
	{
		if(val < lo)
		{
			return lo;
		}
		else if(val > hi)
		{
			return hi;
		}
		else
		{
			return val;
		}
	}
	
	public static byte[] readToBuffer(FileInputStream fis, int fileSize)
	{
		byte[] buf = new byte[fileSize];
				
		try 
		{
			for(int i = 0; i < fileSize; i++)
			{
				buf[i] = (byte)fis.read();
			}
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "There was an error loading your file.");
			System.out.println("readToBuffer");
		}
		
		return buf;
	}
	
	public static byte[] readToBuffer(InputStream fis, int fileSize)
	{
		byte[] buf = new byte[fileSize];
				
		try 
		{
			for(int i = 0; i < fileSize; i++)
			{
				buf[i] = (byte)fis.read();
			}
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "There was an error loading your file.");
			System.out.println("readToBuffer");
		}
		
		return buf;
	}
	
	public static int getFileSize(FileInputStream f)
	{
		int ctr = 0;
		try 
		{
			while(f.read() != -1)
			{
				ctr++;
			}
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "There was an error loading your file.");
			System.out.println("getFileSize");
		}
		
		return ctr;
	}
	
	public static int getFileSize(InputStream f)
	{
		int ctr = 0;
		try 
		{
			while(f.read() != -1)
			{
				ctr++;
			}
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "There was an error loading your file.");
			System.out.println("getFileSize");
		}
		
		return ctr;
	}
}
