/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.knightmare.engine.board;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.knightmare.engine.pieces.Alliance;
import com.knightmare.engine.pieces.Bishop;
import com.knightmare.engine.pieces.King;
import com.knightmare.engine.pieces.Knight;
import com.knightmare.engine.pieces.Pawn;
import com.knightmare.engine.pieces.Piece;
import com.knightmare.engine.pieces.Queen;
import com.knightmare.engine.pieces.Rook;
import com.knightmare.engine.players.BlackPlayer;
import com.knightmare.engine.players.Player;
import com.knightmare.engine.players.WhitePlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 *
 * @author cjyoung
 */
public class Board {
    
    private final List<Square> gameBoard;
    private final Collection<Piece> activeWhitePieces;
    private final Collection<Piece> activeBlackPieces;
    private final Collection<Piece> placeableWhitePieces;
    private final Collection<Piece> placeableBlackPieces;
    
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Pawn enPassantPawn;
    

    private Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.activeWhitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.activeBlackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
        this.placeableWhitePieces = builder.placeableWhitePieces;
        this.placeableBlackPieces = builder.placeableBlackPieces;
        this.enPassantPawn = builder.enPassantPawn;
        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.activeWhitePieces, this.placeableWhitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.activeBlackPieces, this.placeableBlackPieces);
        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.currentMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }
    
    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for(int i=0; i<BoardUtils.NUM_SQUARES; i++){
            final String squareText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", squareText));
            if((i+1)% BoardUtils.NUM_SQUARES_PER_ROW == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }
    
    public Player whitePlayer(){
        return this.whitePlayer;
    }
    
    public Player blackPlayer(){
        return this.blackPlayer;
    }
    
    public Player currentPlayer(){
        return this.currentPlayer;
    }
    
    public Collection<Piece> getActiveBlackPieces(){
        return this.activeBlackPieces;
    }
    
    public Collection<Piece> getActiveWhitePieces(){
        return this.activeWhitePieces;
    }
    
    public Collection<Piece> getPlaceableWhitePieces(){
        return this.placeableWhitePieces;
    }
    
    public Collection<Piece> getPlaceableBlackPieces(){
        return this.placeableBlackPieces;
    }
    
    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }
    
    private static Collection<Piece> calculateActivePieces(final List<Square> gameBoard,
                                                           final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();
        for(final Square square : gameBoard){
            if(square.isOccupied()){
                final Piece piece = square.getPiece();
                if(piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }
    
    private Collection<Move> calculateLegalMoves(final Collection<Piece> activePieces,
                                                 final Collection<Piece> placeablePieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final Piece piece : activePieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));     
        }
        for(final Piece piece : placeablePieces){
            legalMoves.addAll(piece.calculateLegalPlacements(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }
    
    public Square getSquare(final int squareCoordinate) {
        return gameBoard.get(squareCoordinate);
    }

    private static List<Square> createGameBoard(final Builder builder) {
        final List<Square> squares = new ArrayList<>();
        for(int i=0; i<BoardUtils.NUM_SQUARES; i++){
            squares.add(i, Square.createSquare(i, builder.boardConfig.get(i)));
        }
        return (squares);
    }
    
    public static Board createStandardBoard(){
        final Builder builder = new Builder();
       
        // Black Layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(9, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Pawn(51, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Rook(63, Alliance.WHITE));
        
        // White to move
        builder.setMoveMaker(Alliance.WHITE);
        
        return builder.build();
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
    }
    
    public static class Builder {
        
        Map<Integer, Piece> boardConfig;
        List<Piece> placeableWhitePieces;
        List<Piece> placeableBlackPieces;
        Alliance currentMoveMaker;
        Pawn enPassantPawn;
        
        //TODO add method to transfer placeable pieces list to new board
        
        public Builder(){
            this.boardConfig = new HashMap<>();
            this.placeableWhitePieces = new ArrayList<>();
            this.placeableBlackPieces = new ArrayList<>();
        }
        
        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }
        
        public Builder setMoveMaker(final Alliance currentMoveMaker){
            this.currentMoveMaker = currentMoveMaker;
            return this;
        }
       
        public Board build(){
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
        
        public Builder addPlaceablePiece(final Piece piece){
            if(piece.getPieceAlliance() == Alliance.WHITE){
                this.placeableWhitePieces.add(piece);
                return this;
            } else if(piece.getPieceAlliance() == Alliance.BLACK){
                this.placeableBlackPieces.add(piece);
                return this;
            } else {
                throw new RuntimeException("Invalid piece alliance");
            }
        }
        
        // TODO implement placeable piece list management
        public Builder addAllPlaceablePieces(final Collection<Piece> whitePlaceablePieces,
                                             final Collection<Piece> blackPlaceablePieces){
            for(final Piece piece : whitePlaceablePieces){
                this.placeableWhitePieces.add(piece);
            }
            for(final Piece piece : blackPlaceablePieces){
                this.placeableBlackPieces.add(piece);
            }
            return this;
        }
    }
    
}
