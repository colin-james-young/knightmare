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
import static com.knightmare.engine.board.Move.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author cjyoung
 */
public class Pawn extends Piece {
    
    private static final int[] CANDIDATE_MOVE_COORDINATES = { 8, 16, 7, 9 };

    public Pawn(final int piecePosition,
                final Alliance pieceAlliance) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, true);
    }
    
    public Pawn(final int piecePosition,
                final Alliance pieceAlliance,
                final boolean isFirstMove) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int currentCandidateOffset:CANDIDATE_MOVE_COORDINATES){
            
            final int candidateDestCoord = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);
            if(!BoardUtils.isValidSquareCoordinate(candidateDestCoord)){
                continue;
            }
            if(currentCandidateOffset == 8 && !board.getSquare(candidateDestCoord).isOccupied()){
                //TODO deal with promotions
                legalMoves.add(new PawnMove(board, this, candidateDestCoord));
            } else if (currentCandidateOffset == 16 &&
                       this.isFirstMove() &&
                       ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                       (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))){
                // Pawn jump on first move
                final int behindCandidateDestCoord = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if(!board.getSquare(behindCandidateDestCoord).isOccupied() &&
                   !board.getSquare(candidateDestCoord).isOccupied()){
                    legalMoves.add(new PawnJump(board, this, candidateDestCoord));
                }
            } else if(currentCandidateOffset == 7 &&
                      !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
                // Valid capture move
                if(board.getSquare(candidateDestCoord).isOccupied()){
                    final Piece pieceAtDestination = board.getSquare(candidateDestCoord).getPiece();
                    if(this.pieceAlliance != pieceAtDestination.getPieceAlliance()){
                        //TODO 
                        legalMoves.add(new PawnAttackMove(board, this, candidateDestCoord, pieceAtDestination));
                    }
                } else if(board.getEnPassantPawn() != null){
                    if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))){
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestCoord, pieceOnCandidate));
                        }
                    }
                }
            } else if(currentCandidateOffset == 9 &&
                      !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                      (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
                // Valid capture move
                if(board.getSquare(candidateDestCoord).isOccupied()){
                    final Piece pieceAtDestination = board.getSquare(candidateDestCoord).getPiece();
                    if(this.pieceAlliance != pieceAtDestination.getPieceAlliance()){
                        //TODO
                        legalMoves.add(new PawnAttackMove(board, this, candidateDestCoord, pieceAtDestination));
                    }
                } else if(board.getEnPassantPawn() != null){
                    if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))){
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestCoord, pieceOnCandidate));
                        }
                    }
                }
            } 
        }
        
        return ImmutableList.copyOf(legalMoves);
    }
    
    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
    
    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public Pawn changeToPlaceable() {
        return new Pawn(-1, this.getPieceAlliance().opposite());
    }

}
