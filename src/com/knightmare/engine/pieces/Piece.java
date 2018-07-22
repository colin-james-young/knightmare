/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.knightmare.engine.pieces;

import com.google.common.collect.ImmutableList;
import com.knightmare.engine.board.Board;
import com.knightmare.engine.board.BoardUtils;
import com.knightmare.engine.board.Move;
import com.knightmare.engine.board.Move.PlacementMove;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author cjyoung
 */
public abstract class Piece {
    
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;
    
    Piece(final PieceType pieceType,
          final int piecePosition,
          final Alliance pieceAlliance,
          final boolean isFirstMove){
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }
    
    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31* result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }
    
    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
               pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }
    
    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }
    
    public int getPiecePosition(){
        return this.piecePosition;
    }
    
    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }
    
    public boolean isFirstMove(){
        return this.isFirstMove;
    }
    
    public PieceType getPieceType(){
        return this.pieceType;
    }
    
    public int getPieceValue(){
        return this.pieceType.getPieceValue();
    }
    
    public abstract Collection<Move> calculateLegalMoves(final Board board);
    public abstract Piece movePiece(Move move);
    public abstract Piece changeToPlaceable();
    
    public Collection<Move> calculateLegalPlacements(final Board board){
        List<Move> legalPlacements = new ArrayList<>();
        for(int i=0; i<BoardUtils.NUM_SQUARES; i++){
            if(!board.getSquare(i).isOccupied()){
                legalPlacements.add(new PlacementMove(board, this, i));
            }
        }        
        return ImmutableList.copyOf(legalPlacements);
    }

    public enum PieceType{
        PAWN("P", 100) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N", 300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B", 300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R", 500) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q", 900) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K", 10000) {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };
        
        private String pieceName;
        private int pieceValue;
        
        PieceType(final String pieceName,
                  final int pieceValue){
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }
        
        @Override
        public String toString(){
            return this.pieceName;
        }
        
        public int getPieceValue(){
            return this.pieceValue;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();
    
    }
    
}
