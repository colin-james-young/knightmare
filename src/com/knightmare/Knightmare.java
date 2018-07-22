/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.knightmare;

import com.knightmare.engine.board.Board;
import com.knightmare.gui.Table;

/**
 *
 * @author cjyoung
 */
public class Knightmare {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Board board = Board.createStandardBoard();
        
        System.out.println(board);
        
        Table table = new Table();
    }
    
}
