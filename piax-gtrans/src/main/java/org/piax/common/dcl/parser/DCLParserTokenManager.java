/* Generated By:JJTree&JavaCC: Do not edit this line. DCLParserTokenManager.java */
package org.piax.common.dcl.parser;
import org.piax.common.dcl.*;

/** Token Manager. */
@SuppressWarnings("all")
public class DCLParserTokenManager implements DCLParserConstants
{

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x1f0000L) != 0L)
         {
            jjmatchedKind = 33;
            return 56;
         }
         if ((active0 & 0x8000L) != 0L)
            return 1;
         if ((active0 & 0x1000000L) != 0L)
            return 28;
         return -1;
      case 1:
         if ((active0 & 0x160000L) != 0L)
            return 56;
         if ((active0 & 0x90000L) != 0L)
         {
            if (jjmatchedPos != 1)
            {
               jjmatchedKind = 33;
               jjmatchedPos = 1;
            }
            return 56;
         }
         return -1;
      case 2:
         if ((active0 & 0x180000L) != 0L)
         {
            jjmatchedKind = 33;
            jjmatchedPos = 2;
            return 56;
         }
         if ((active0 & 0x10000L) != 0L)
            return 56;
         return -1;
      case 3:
         if ((active0 & 0x180000L) != 0L)
         {
            jjmatchedKind = 33;
            jjmatchedPos = 3;
            return 56;
         }
         return -1;
      case 4:
         if ((active0 & 0x180000L) != 0L)
         {
            jjmatchedKind = 33;
            jjmatchedPos = 4;
            return 56;
         }
         return -1;
      case 5:
         if ((active0 & 0x180000L) != 0L)
         {
            jjmatchedKind = 33;
            jjmatchedPos = 5;
            return 56;
         }
         return -1;
      case 6:
         if ((active0 & 0x180000L) != 0L)
         {
            jjmatchedKind = 33;
            jjmatchedPos = 6;
            return 56;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 37:
         return jjStopAtPos(0, 25);
      case 40:
         return jjStopAtPos(0, 8);
      case 41:
         return jjStopAtPos(0, 9);
      case 42:
         return jjStopAtPos(0, 23);
      case 43:
         return jjStopAtPos(0, 21);
      case 44:
         return jjStopAtPos(0, 14);
      case 45:
         return jjStopAtPos(0, 22);
      case 46:
         return jjMoveStringLiteralDfa1_0(0x8000L);
      case 47:
         return jjStartNfaWithStates_0(0, 24, 28);
      case 91:
         return jjStopAtPos(0, 10);
      case 93:
         return jjStopAtPos(0, 11);
      case 97:
         return jjMoveStringLiteralDfa1_0(0x10000L);
      case 101:
         return jjMoveStringLiteralDfa1_0(0x20000L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x140000L);
      case 111:
         return jjMoveStringLiteralDfa1_0(0x80000L);
      case 123:
         return jjStopAtPos(0, 12);
      case 125:
         return jjStopAtPos(0, 13);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 46:
         if ((active0 & 0x8000L) != 0L)
            return jjStopAtPos(1, 15);
         break;
      case 110:
         if ((active0 & 0x40000L) != 0L)
         {
            jjmatchedKind = 18;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0x110000L);
      case 113:
         if ((active0 & 0x20000L) != 0L)
            return jjStartNfaWithStates_0(1, 17, 56);
         break;
      case 118:
         return jjMoveStringLiteralDfa2_0(active0, 0x80000L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 99:
         return jjMoveStringLiteralDfa3_0(active0, 0x100000L);
      case 100:
         if ((active0 & 0x10000L) != 0L)
            return jjStartNfaWithStates_0(2, 16, 56);
         break;
      case 101:
         return jjMoveStringLiteralDfa3_0(active0, 0x80000L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 108:
         return jjMoveStringLiteralDfa4_0(active0, 0x100000L);
      case 114:
         return jjMoveStringLiteralDfa4_0(active0, 0x80000L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 108:
         return jjMoveStringLiteralDfa5_0(active0, 0x80000L);
      case 117:
         return jjMoveStringLiteralDfa5_0(active0, 0x100000L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa6_0(active0, 0x80000L);
      case 100:
         return jjMoveStringLiteralDfa6_0(active0, 0x100000L);
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
private int jjMoveStringLiteralDfa6_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 101:
         return jjMoveStringLiteralDfa7_0(active0, 0x100000L);
      case 112:
         return jjMoveStringLiteralDfa7_0(active0, 0x80000L);
      default :
         break;
   }
   return jjStartNfa_0(5, active0);
}
private int jjMoveStringLiteralDfa7_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(5, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(6, active0);
      return 7;
   }
   switch(curChar)
   {
      case 115:
         if ((active0 & 0x80000L) != 0L)
            return jjStartNfaWithStates_0(7, 19, 56);
         else if ((active0 & 0x100000L) != 0L)
            return jjStartNfaWithStates_0(7, 20, 56);
         break;
      default :
         break;
   }
   return jjStartNfa_0(6, active0);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec2 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 56;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 56:
                  if ((0x3ff001000000000L & l) != 0L)
                  {
                     if (kind > 33)
                        kind = 33;
                     jjCheckNAddTwoStates(17, 18);
                  }
                  else if (curChar == 46)
                     jjCheckNAdd(18);
                  break;
               case 28:
                  if (curChar == 42)
                     jjCheckNAddTwoStates(34, 35);
                  else if (curChar == 47)
                     jjCheckNAddStates(0, 2);
                  break;
               case 0:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 26)
                        kind = 26;
                     jjCheckNAddStates(3, 12);
                  }
                  else if (curChar == 47)
                     jjAddStates(13, 14);
                  else if (curChar == 36)
                  {
                     if (kind > 33)
                        kind = 33;
                     jjCheckNAddTwoStates(17, 18);
                  }
                  else if (curChar == 34)
                     jjCheckNAddStates(15, 17);
                  else if (curChar == 46)
                     jjCheckNAdd(1);
                  if (curChar == 48)
                     jjAddStates(18, 19);
                  break;
               case 1:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 30)
                     kind = 30;
                  jjCheckNAddStates(20, 22);
                  break;
               case 3:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(4);
                  break;
               case 4:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 30)
                     kind = 30;
                  jjCheckNAddTwoStates(4, 5);
                  break;
               case 6:
                  if (curChar == 34)
                     jjCheckNAddStates(15, 17);
                  break;
               case 7:
                  if ((0xfffffffbffffdbffL & l) != 0L)
                     jjCheckNAddStates(15, 17);
                  break;
               case 9:
                  if ((0x8400000000L & l) != 0L)
                     jjCheckNAddStates(15, 17);
                  break;
               case 10:
                  if (curChar == 34 && kind > 32)
                     kind = 32;
                  break;
               case 12:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 13;
                  break;
               case 13:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 14;
                  break;
               case 14:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 15;
                  break;
               case 15:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddStates(15, 17);
                  break;
               case 16:
                  if (curChar != 36)
                     break;
                  if (kind > 33)
                     kind = 33;
                  jjCheckNAddTwoStates(17, 18);
                  break;
               case 17:
                  if (curChar == 46)
                     jjCheckNAdd(18);
                  break;
               case 18:
                  if ((0x3ff001000000000L & l) == 0L)
                     break;
                  if (kind > 33)
                     kind = 33;
                  jjCheckNAddTwoStates(17, 18);
                  break;
               case 19:
                  if (curChar == 48)
                     jjAddStates(18, 19);
                  break;
               case 21:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 26)
                     kind = 26;
                  jjCheckNAddStates(23, 25);
                  break;
               case 25:
                  if ((0x3000000000000L & l) == 0L)
                     break;
                  if (kind > 26)
                     kind = 26;
                  jjCheckNAddStates(26, 28);
                  break;
               case 27:
                  if (curChar == 47)
                     jjAddStates(13, 14);
                  break;
               case 29:
                  if ((0xffffffffffffdbffL & l) != 0L)
                     jjCheckNAddStates(0, 2);
                  break;
               case 30:
                  if ((0x2400L & l) != 0L && kind > 1)
                     kind = 1;
                  break;
               case 31:
                  if (curChar == 10 && kind > 1)
                     kind = 1;
                  break;
               case 32:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 31;
                  break;
               case 33:
                  if (curChar == 42)
                     jjCheckNAddTwoStates(34, 35);
                  break;
               case 34:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(34, 35);
                  break;
               case 35:
                  if (curChar == 42)
                     jjCheckNAddStates(29, 31);
                  break;
               case 36:
                  if ((0xffff7bffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(37, 35);
                  break;
               case 37:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(37, 35);
                  break;
               case 38:
                  if (curChar == 47 && kind > 2)
                     kind = 2;
                  break;
               case 39:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 26)
                     kind = 26;
                  jjCheckNAddStates(3, 12);
                  break;
               case 41:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 26)
                     kind = 26;
                  jjCheckNAddStates(32, 34);
                  break;
               case 42:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(42, 43);
                  break;
               case 43:
                  if (curChar == 46)
                     jjCheckNAdd(44);
                  break;
               case 44:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 30)
                     kind = 30;
                  jjCheckNAddStates(35, 37);
                  break;
               case 46:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(47);
                  break;
               case 47:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 30)
                     kind = 30;
                  jjCheckNAddTwoStates(47, 5);
                  break;
               case 48:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(48, 49);
                  break;
               case 50:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(51);
                  break;
               case 51:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 30)
                     kind = 30;
                  jjCheckNAddTwoStates(51, 5);
                  break;
               case 52:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddStates(38, 40);
                  break;
               case 54:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(55);
                  break;
               case 55:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(55, 5);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 56:
               case 18:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 33)
                     kind = 33;
                  jjCheckNAddTwoStates(17, 18);
                  break;
               case 0:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 33)
                     kind = 33;
                  jjCheckNAddTwoStates(17, 18);
                  break;
               case 2:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(41, 42);
                  break;
               case 5:
                  if ((0x5000000050L & l) != 0L && kind > 30)
                     kind = 30;
                  break;
               case 7:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjCheckNAddStates(15, 17);
                  break;
               case 8:
                  if (curChar == 92)
                     jjAddStates(43, 44);
                  break;
               case 9:
                  if ((0x14404410000000L & l) != 0L)
                     jjCheckNAddStates(15, 17);
                  break;
               case 11:
                  if (curChar == 117)
                     jjstateSet[jjnewStateCnt++] = 12;
                  break;
               case 12:
                  if ((0x7e0000007eL & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 13;
                  break;
               case 13:
                  if ((0x7e0000007eL & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 14;
                  break;
               case 14:
                  if ((0x7e0000007eL & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 15;
                  break;
               case 15:
                  if ((0x7e0000007eL & l) != 0L)
                     jjCheckNAddStates(15, 17);
                  break;
               case 20:
                  if ((0x100000001000000L & l) != 0L)
                     jjCheckNAdd(21);
                  break;
               case 21:
                  if ((0x7e0000007eL & l) == 0L)
                     break;
                  if (kind > 26)
                     kind = 26;
                  jjCheckNAddStates(23, 25);
                  break;
               case 22:
                  if (curChar == 95)
                     jjCheckNAdd(21);
                  break;
               case 23:
                  if ((0x108000001080L & l) != 0L && kind > 26)
                     kind = 26;
                  break;
               case 24:
                  if ((0x400000004L & l) != 0L)
                     jjCheckNAdd(25);
                  break;
               case 26:
                  if (curChar == 95)
                     jjCheckNAdd(25);
                  break;
               case 29:
                  jjAddStates(0, 2);
                  break;
               case 34:
                  jjCheckNAddTwoStates(34, 35);
                  break;
               case 36:
               case 37:
                  jjCheckNAddTwoStates(37, 35);
                  break;
               case 40:
                  if (curChar == 95)
                     jjstateSet[jjnewStateCnt++] = 41;
                  break;
               case 45:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(45, 46);
                  break;
               case 49:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(47, 48);
                  break;
               case 53:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(49, 50);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (int)(curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 7:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                     jjAddStates(15, 17);
                  break;
               case 29:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                     jjAddStates(0, 2);
                  break;
               case 34:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                     jjCheckNAddTwoStates(34, 35);
                  break;
               case 36:
               case 37:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                     jjCheckNAddTwoStates(37, 35);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 56 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   29, 30, 32, 40, 41, 23, 42, 43, 48, 49, 52, 53, 5, 28, 33, 7, 
   8, 10, 20, 24, 1, 2, 5, 22, 21, 23, 26, 25, 23, 35, 36, 38, 
   40, 41, 23, 44, 45, 5, 52, 53, 5, 3, 4, 9, 11, 46, 47, 50, 
   51, 54, 55, 
};
private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec2[i2] & l2) != 0L);
      default :
         if ((jjbitVec0[i1] & l1) != 0L)
            return true;
         return false;
   }
}

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, "\50", "\51", "\133", "\135", 
"\173", "\175", "\54", "\56\56", "\141\156\144", "\145\161", "\151\156", 
"\157\166\145\162\154\141\160\163", "\151\156\143\154\165\144\145\163", "\53", "\55", "\52", "\57", "\45", null, 
null, null, null, null, null, null, null, null, null, null, };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
static final long[] jjtoToken = {
   0x347ffff01L, 
};
static final long[] jjtoSkip = {
   0xfeL, 
};
protected JavaCharStream input_stream;
private final int[] jjrounds = new int[56];
private final int[] jjstateSet = new int[112];
protected char curChar;
/** Constructor. */
public DCLParserTokenManager(JavaCharStream stream){
   if (JavaCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}

/** Constructor. */
public DCLParserTokenManager(JavaCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
public void ReInit(JavaCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 56; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
public void ReInit(JavaCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100003600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

}
