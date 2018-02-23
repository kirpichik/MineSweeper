package org.polushin.minesweeper;

public class Main {

	public static void main(String[] args) {
		//System.out.println(Integer.toString(0xffff, 2));
		System.out.println(Integer.toString((1 << 16) | (5 & 0xffff), 2));
	}
}
