/*
BSD License

Copyright (c) 2013, Tom Everett
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. Neither the name of Tom Everett nor the names of its contributors
   may be used to endorse or promote products derived from this software
   without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

grammar mips;

prog
    : (line? EOL)+
    ;

line
    : comment
    | instruction
    | lbl
    ;

instruction
    : label? opcode argumentlist? comment?
    ;


lbl
    : label ':'
    ;

argumentlist
    : argument (',' argumentlist)?
    ;

label
    : name
    ;

argument
    : prefix? (number | name | string | '*') (('+' | '-') number)?
    | '(' argument ')'
    ;

prefix
    : '#'
    | 'R'
    ;

string
    : STRING
    ;

name
    : NAME
    ;

number
    : NUMBER
    ;

comment
    : COMMENT
    ;

opcode
    : DADDU
    | DMULT
    | OR
    | SLT
    | BEQ
    | LW
    | LWU
    | SW
    | DSLL
    | ANDI
    | DADDIU
    | J
    | LDOTS
    | SDOTS
    | ADDDOTS
    | MULDOTS
    ;

fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
//
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');
fragment DOT:('\\.');

/*
* opcodes
*/
DADDU  : D A D D U   ;
DMULT  : D M U L T   ;
OR     : O R         ;
SLT    : S L T       ;
BEQ    : B E Q       ;
LW     : L W         ;
LWU    : L W U       ;
SW     : S W         ;
DSLL   : D S L L     ;
ANDI   : A N D I     ;
DADDIU : D A D D I U ;
J      : ('j'|'J')    ;
LDOTS    : L DOT S     ;
SDOTS    : S DOT S     ;
ADDDOTS  : A D D DOT S ;
MULDOTS  : M U L DOT S ;

NAME
    : [a-zA-Z] [a-zA-Z0-9."]*
    ;

NUMBER
    : '$'? [0-9a-fA-F]+
    ;

COMMENT
    : ';' ~[\r\n]* ->skip
    ;

STRING
    : '"' ~["]* '"'
    ;

EOL
    : '\r'? '\n'
    ;

WS
    : [ \t]->skip
    ;