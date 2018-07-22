/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.knightmare.engine.pieces;

import com.knightmare.engine.players.BlackPlayer;
import com.knightmare.engine.players.Player;
import com.knightmare.engine.players.WhitePlayer;

/**
 *
 * @author cjyoung
 */
public enum Alliance {
    WHITE {

        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }
        
        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,
                                   final BlackPlayer blackPlayer) {
            return whitePlayer;
        }

        @Override
        public Alliance opposite() {
            return BLACK;
        }
    },
    BLACK {

        @Override
        public int getDirection() {
            return 1;        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }
        
        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,
                                   final BlackPlayer blackPlayer) {
            return blackPlayer;
        }

        @Override
        public Alliance opposite() {
            return WHITE;
        }
    };
    
    public abstract int getDirection();
    public abstract int getOppositeDirection();
    public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract Alliance opposite();
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
