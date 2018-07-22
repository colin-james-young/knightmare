/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.knightmare.engine.players;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.knightmare.engine.board.Board;
import com.knightmare.engine.board.Move;
import com.knightmare.engine.pieces.Alliance;
import com.knightmare.engine.pieces.King;
import com.knightmare.engine.pieces.Piece;
import com.knightmare.engine.pieces.Piece.PieceType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author cjyoung
 */
public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;
    
    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves){
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves)));
        this.isInCheck = !Player.calculateAttacksOnSquare(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }
    
    public King getPlayerKing(){
        return this.playerKing;
    }
    
    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }
    
    protected static Collection<Move> calculateAttacksOnSquare(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move : moves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    private King establishKing() {
        for(final Piece piece : getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Not a valid board");
    }
    
    public boolean isMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }
    
    public boolean isInCheck(){
        return this.isInCheck;
    }
    
    public boolean isInCheckmate(){
        return this.isInCheck() && !hasEscapeMoves();
    }
    
    public boolean isInStalemate(){
        return !this.isInCheck && !hasEscapeMoves();
    }
    
    //TODO finish implementation of methods below
    //TODO Improve efficiency by only evaluating moves on key squares/pieces
    protected boolean hasEscapeMoves() {
        for(final Move move : this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }
    
    public boolean isCastled(){
        return false;
    }
    
    public MoveTransition makeMove(final Move move){
        if(!isMoveLegal(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();
        
        final Collection<Move> kingAttacks = Player.calculateAttacksOnSquare(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
              transitionBoard.currentPlayer().getLegalMoves());
        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces(); 
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);
}
