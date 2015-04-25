package com.laituanmanh.AI2048.main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.laituanmanh.AI2048.ui.GameUIAdapter;

public class MainClass {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GameUIAdapter ui = new GameUIAdapter();
		ui.setVisible(true);
	}
}
