/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.knightmare.engine.players;

import com.google.common.collect.ImmutableList;
import com.knightmare.engine.board.Board;
import com.knightmare.engine.board.Move;
import static com.knightmare.engine.board.Move.*;
import com.knightmare.engine.board.Square;
import com.knightmare.engine.pieces.Alliance;
import com.knightmare.engine.pieces.Piece;
import com.knightmare.engine.pieces.Rook;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author cjyoung
 */
public class BlackPlayer extends Player{

    public BlackPlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getActiveBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }
    
    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            // Black king side castle
            if(!this.board.getSquare(5).isOccupied() && !this.board.getSquare(6).isOccupied()){
                final Square rookSquare = this.board.getSquare(7);
                //Null pointer error possibility?
                if(rookSquare.isOccupied() && rookSquare.getPiece().isFirstMove()){
                    if(Player.calculateAttacksOnSquare(5, opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnSquare(6, opponentLegals).isEmpty() &&
                       rookSquare.getPiece().getPieceType().isRook()){
                        kingCastles.add(new KingSideCastleMove(this.board,
                                                               this.playerKing,
                                                               6,
                                                               (Rook) rookSquare.getPiece(),
                                                               rookSquare.getSquareCoordinate(),
                                                               5));                      
                    }
                }
            }
            // Black queen side castle
            if(!this.board.getSquare(1).isOccupied() &&
               !this.board.getSquare(2).isOccupied() &&
               !this.board.getSquare(3).isOccupied()){
                final Square rookSquare = this.board.getSquare(0);
                //Null pointer error possibility?
                if(rookSquare.isOccupied() && rookSquare.getPiece().isFirstMove() &&
                    Player.calculateAttacksOnSquare(2, opponentLegals).isEmpty() &&
                    Player.calculateAttacksOnSquare(3, opponentLegals).isEmpty() &&
                    rookSquare.getPiece().getPieceType().isRook()){
                    kingCastles.add(new QueenSideCastleMove(this.board,
                                                            this.playerKing,
                                                            2,
                                                            (Rook) rookSquare.getPiece(),
                                                            rookSquare.getSquareCoordinate(),
                                                            3));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
    
}
