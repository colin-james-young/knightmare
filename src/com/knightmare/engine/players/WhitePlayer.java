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
public class WhitePlayer extends Player{

    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getActiveWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            // White king side castle
            if(!this.board.getSquare(61).isOccupied() && !this.board.getSquare(62).isOccupied()){
                final Square rookSquare = this.board.getSquare(63);
                //Null pointer error possibility?
                if(rookSquare.isOccupied() && rookSquare.getPiece().isFirstMove()){
                    if(Player.calculateAttacksOnSquare(61, opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnSquare(62, opponentLegals).isEmpty() &&
                       rookSquare.getPiece().getPieceType().isRook()){
                        kingCastles.add(new KingSideCastleMove(this.board,
                                                               this.playerKing,
                                                               62,
                                                               (Rook) rookSquare.getPiece(),
                                                               rookSquare.getSquareCoordinate(),
                                                               61));                      
                    }
                }
            }
            // White queen side castle
            if(!this.board.getSquare(59).isOccupied() &&
               !this.board.getSquare(58).isOccupied() &&
               !this.board.getSquare(57).isOccupied()){
                final Square rookSquare = this.board.getSquare(56);
                //Null pointer error possibility?
                if(rookSquare.isOccupied() && rookSquare.getPiece().isFirstMove() &&
                    Player.calculateAttacksOnSquare(58, opponentLegals).isEmpty() &&
                    Player.calculateAttacksOnSquare(59, opponentLegals).isEmpty() &&
                    rookSquare.getPiece().getPieceType().isRook()){
                    kingCastles.add(new QueenSideCastleMove(this.board,
                                                            this.playerKing,
                                                            58,
                                                            (Rook) rookSquare.getPiece(),
                                                            rookSquare.getSquareCoordinate(),
                                                            59));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
