package com.knightmare.engine.board;

import com.google.common.collect.ImmutableMap;
import com.knightmare.engine.pieces.Piece;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cjyoung
 */

public abstract class Square {
    
    protected final int squareCoordinate;
    
    private static final Map<Integer, EmptySquare> EMPTY_SQUARES_CACHE = createAllPossibleEmptySquares();
  
    public abstract boolean isOccupied();
    public abstract Piece getPiece();

    private static Map<Integer, EmptySquare> createAllPossibleEmptySquares() {
        
        final Map<Integer, EmptySquare> emptySquareMap = new HashMap<>();
        
        for(int i=0; i<BoardUtils.NUM_SQUARES; i++){
            emptySquareMap.put(i, new EmptySquare(i));
        }
        
        return ImmutableMap.copyOf(emptySquareMap);
    }
    
      private Square(final int coordinate){
        this.squareCoordinate = coordinate;
    }
      
    public static Square createSquare(final int coordinate, final Piece piece){
        return piece != null ? new OccupiedSquare(coordinate, piece) : EMPTY_SQUARES_CACHE.get(coordinate);
    }
    
    public int getSquareCoordinate(){
        return this.squareCoordinate;
    }
    
    public static final class EmptySquare extends Square {
        
        private EmptySquare(final int coordinate){
            super(coordinate);
        }
        
        @Override
        public String toString(){
            return "-";
        }
        
        @Override
        public boolean isOccupied() {
            return false;
        }
        
        @Override
        public Piece getPiece(){
            return null;
        }
    }
    
    public static final class OccupiedSquare extends Square{
        private final Piece pieceOnSquare;
        
        private OccupiedSquare(final int coordinate, final Piece pieceOnSquare){
            super(coordinate);
            this.pieceOnSquare = pieceOnSquare;
        }
        
        @Override
        public String toString(){
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() : 
                   getPiece().toString();
        }
        
        @Override
        public boolean isOccupied(){
            return true;
        }
        
        @Override
        public Piece getPiece(){
            return this.pieceOnSquare;
        }
    }
}
