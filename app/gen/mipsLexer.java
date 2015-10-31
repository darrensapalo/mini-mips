// Generated from C:/Users/Darren/git/mini-mips/app\mips.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class mipsLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		DADDU=10, DMULT=11, OR=12, SLT=13, BEQ=14, LW=15, LWU=16, SW=17, DSLL=18, 
		ANDI=19, DADDIU=20, J=21, LDOTS=22, SDOTS=23, ADDDOTS=24, MULDOTS=25, 
		NAME=26, NUMBER=27, COMMENT=28, STRING=29, EOL=30, WS=31;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"A", "B", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M", "N", "O", 
		"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "DOT", "DADDU", 
		"DMULT", "OR", "SLT", "BEQ", "LW", "LWU", "SW", "DSLL", "ANDI", "DADDIU", 
		"J", "LDOTS", "SDOTS", "ADDDOTS", "MULDOTS", "NAME", "NUMBER", "COMMENT", 
		"STRING", "EOL", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "':'", "','", "'*'", "'+'", "'-'", "'('", "')'", "'#'", "'R'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, "DADDU", "DMULT", 
		"OR", "SLT", "BEQ", "LW", "LWU", "SW", "DSLL", "ANDI", "DADDIU", "J", 
		"LDOTS", "SDOTS", "ADDDOTS", "MULDOTS", "NAME", "NUMBER", "COMMENT", "STRING", 
		"EOL", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public mipsLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "mips.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2!\u012e\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\3\2\3\2\3\3\3\3\3"+
		"\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3"+
		"\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23"+
		"\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32"+
		"\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\""+
		"\3#\3#\3$\3$\3$\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3\'\3\'\3\'\3(\3("+
		"\3(\3(\3)\3)\3)\3)\3*\3*\3*\3+\3+\3+\3+\3,\3,\3,\3-\3-\3-\3-\3-\3.\3."+
		"\3.\3.\3.\3/\3/\3/\3/\3/\3/\3/\3\60\3\60\3\61\3\61\3\61\3\61\3\62\3\62"+
		"\3\62\3\62\3\63\3\63\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\64\3\64"+
		"\3\65\3\65\7\65\u0107\n\65\f\65\16\65\u010a\13\65\3\66\5\66\u010d\n\66"+
		"\3\66\6\66\u0110\n\66\r\66\16\66\u0111\3\67\3\67\7\67\u0116\n\67\f\67"+
		"\16\67\u0119\13\67\3\67\3\67\38\38\78\u011f\n8\f8\168\u0122\138\38\38"+
		"\39\59\u0127\n9\39\39\3:\3:\3:\3:\2\2;\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\2\27\2\31\2\33\2\35\2\37\2!\2#\2%\2\'\2)\2+\2-\2/\2\61\2\63"+
		"\2\65\2\67\29\2;\2=\2?\2A\2C\2E\2G\2I\fK\rM\16O\17Q\20S\21U\22W\23Y\24"+
		"[\25]\26_\27a\30c\31e\32g\33i\34k\35m\36o\37q s!\3\2\"\4\2CCcc\4\2DDd"+
		"d\4\2EEee\4\2FFff\4\2GGgg\4\2HHhh\4\2IIii\4\2JJjj\4\2KKkk\4\2MMmm\4\2"+
		"NNnn\4\2OOoo\4\2PPpp\4\2QQqq\4\2RRrr\4\2SSss\4\2TTtt\4\2UUuu\4\2VVvv\4"+
		"\2WWww\4\2XXxx\4\2YYyy\4\2ZZzz\4\2[[{{\4\2\\\\||\4\2LLll\4\2C\\c|\7\2"+
		"$$\60\60\62;C\\c|\5\2\62;CHch\4\2\f\f\17\17\3\2$$\4\2\13\13\"\"\u0119"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3"+
		"\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2"+
		"\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2"+
		"g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3"+
		"\2\2\2\3u\3\2\2\2\5w\3\2\2\2\7y\3\2\2\2\t{\3\2\2\2\13}\3\2\2\2\r\177\3"+
		"\2\2\2\17\u0081\3\2\2\2\21\u0083\3\2\2\2\23\u0085\3\2\2\2\25\u0087\3\2"+
		"\2\2\27\u0089\3\2\2\2\31\u008b\3\2\2\2\33\u008d\3\2\2\2\35\u008f\3\2\2"+
		"\2\37\u0091\3\2\2\2!\u0093\3\2\2\2#\u0095\3\2\2\2%\u0097\3\2\2\2\'\u0099"+
		"\3\2\2\2)\u009b\3\2\2\2+\u009d\3\2\2\2-\u009f\3\2\2\2/\u00a1\3\2\2\2\61"+
		"\u00a3\3\2\2\2\63\u00a5\3\2\2\2\65\u00a7\3\2\2\2\67\u00a9\3\2\2\29\u00ab"+
		"\3\2\2\2;\u00ad\3\2\2\2=\u00af\3\2\2\2?\u00b1\3\2\2\2A\u00b3\3\2\2\2C"+
		"\u00b5\3\2\2\2E\u00b7\3\2\2\2G\u00b9\3\2\2\2I\u00bc\3\2\2\2K\u00c2\3\2"+
		"\2\2M\u00c8\3\2\2\2O\u00cb\3\2\2\2Q\u00cf\3\2\2\2S\u00d3\3\2\2\2U\u00d6"+
		"\3\2\2\2W\u00da\3\2\2\2Y\u00dd\3\2\2\2[\u00e2\3\2\2\2]\u00e7\3\2\2\2_"+
		"\u00ee\3\2\2\2a\u00f0\3\2\2\2c\u00f4\3\2\2\2e\u00f8\3\2\2\2g\u00fe\3\2"+
		"\2\2i\u0104\3\2\2\2k\u010c\3\2\2\2m\u0113\3\2\2\2o\u011c\3\2\2\2q\u0126"+
		"\3\2\2\2s\u012a\3\2\2\2uv\7<\2\2v\4\3\2\2\2wx\7.\2\2x\6\3\2\2\2yz\7,\2"+
		"\2z\b\3\2\2\2{|\7-\2\2|\n\3\2\2\2}~\7/\2\2~\f\3\2\2\2\177\u0080\7*\2\2"+
		"\u0080\16\3\2\2\2\u0081\u0082\7+\2\2\u0082\20\3\2\2\2\u0083\u0084\7%\2"+
		"\2\u0084\22\3\2\2\2\u0085\u0086\7T\2\2\u0086\24\3\2\2\2\u0087\u0088\t"+
		"\2\2\2\u0088\26\3\2\2\2\u0089\u008a\t\3\2\2\u008a\30\3\2\2\2\u008b\u008c"+
		"\t\4\2\2\u008c\32\3\2\2\2\u008d\u008e\t\5\2\2\u008e\34\3\2\2\2\u008f\u0090"+
		"\t\6\2\2\u0090\36\3\2\2\2\u0091\u0092\t\7\2\2\u0092 \3\2\2\2\u0093\u0094"+
		"\t\b\2\2\u0094\"\3\2\2\2\u0095\u0096\t\t\2\2\u0096$\3\2\2\2\u0097\u0098"+
		"\t\n\2\2\u0098&\3\2\2\2\u0099\u009a\t\13\2\2\u009a(\3\2\2\2\u009b\u009c"+
		"\t\f\2\2\u009c*\3\2\2\2\u009d\u009e\t\r\2\2\u009e,\3\2\2\2\u009f\u00a0"+
		"\t\16\2\2\u00a0.\3\2\2\2\u00a1\u00a2\t\17\2\2\u00a2\60\3\2\2\2\u00a3\u00a4"+
		"\t\20\2\2\u00a4\62\3\2\2\2\u00a5\u00a6\t\21\2\2\u00a6\64\3\2\2\2\u00a7"+
		"\u00a8\t\22\2\2\u00a8\66\3\2\2\2\u00a9\u00aa\t\23\2\2\u00aa8\3\2\2\2\u00ab"+
		"\u00ac\t\24\2\2\u00ac:\3\2\2\2\u00ad\u00ae\t\25\2\2\u00ae<\3\2\2\2\u00af"+
		"\u00b0\t\26\2\2\u00b0>\3\2\2\2\u00b1\u00b2\t\27\2\2\u00b2@\3\2\2\2\u00b3"+
		"\u00b4\t\30\2\2\u00b4B\3\2\2\2\u00b5\u00b6\t\31\2\2\u00b6D\3\2\2\2\u00b7"+
		"\u00b8\t\32\2\2\u00b8F\3\2\2\2\u00b9\u00ba\7^\2\2\u00ba\u00bb\7\60\2\2"+
		"\u00bbH\3\2\2\2\u00bc\u00bd\5\33\16\2\u00bd\u00be\5\25\13\2\u00be\u00bf"+
		"\5\33\16\2\u00bf\u00c0\5\33\16\2\u00c0\u00c1\5;\36\2\u00c1J\3\2\2\2\u00c2"+
		"\u00c3\5\33\16\2\u00c3\u00c4\5+\26\2\u00c4\u00c5\5;\36\2\u00c5\u00c6\5"+
		")\25\2\u00c6\u00c7\59\35\2\u00c7L\3\2\2\2\u00c8\u00c9\5/\30\2\u00c9\u00ca"+
		"\5\65\33\2\u00caN\3\2\2\2\u00cb\u00cc\5\67\34\2\u00cc\u00cd\5)\25\2\u00cd"+
		"\u00ce\59\35\2\u00ceP\3\2\2\2\u00cf\u00d0\5\27\f\2\u00d0\u00d1\5\35\17"+
		"\2\u00d1\u00d2\5\63\32\2\u00d2R\3\2\2\2\u00d3\u00d4\5)\25\2\u00d4\u00d5"+
		"\5? \2\u00d5T\3\2\2\2\u00d6\u00d7\5)\25\2\u00d7\u00d8\5? \2\u00d8\u00d9"+
		"\5;\36\2\u00d9V\3\2\2\2\u00da\u00db\5\67\34\2\u00db\u00dc\5? \2\u00dc"+
		"X\3\2\2\2\u00dd\u00de\5\33\16\2\u00de\u00df\5\67\34\2\u00df\u00e0\5)\25"+
		"\2\u00e0\u00e1\5)\25\2\u00e1Z\3\2\2\2\u00e2\u00e3\5\25\13\2\u00e3\u00e4"+
		"\5-\27\2\u00e4\u00e5\5\33\16\2\u00e5\u00e6\5%\23\2\u00e6\\\3\2\2\2\u00e7"+
		"\u00e8\5\33\16\2\u00e8\u00e9\5\25\13\2\u00e9\u00ea\5\33\16\2\u00ea\u00eb"+
		"\5\33\16\2\u00eb\u00ec\5%\23\2\u00ec\u00ed\5;\36\2\u00ed^\3\2\2\2\u00ee"+
		"\u00ef\t\33\2\2\u00ef`\3\2\2\2\u00f0\u00f1\5)\25\2\u00f1\u00f2\5G$\2\u00f2"+
		"\u00f3\5\67\34\2\u00f3b\3\2\2\2\u00f4\u00f5\5\67\34\2\u00f5\u00f6\5G$"+
		"\2\u00f6\u00f7\5\67\34\2\u00f7d\3\2\2\2\u00f8\u00f9\5\25\13\2\u00f9\u00fa"+
		"\5\33\16\2\u00fa\u00fb\5\33\16\2\u00fb\u00fc\5G$\2\u00fc\u00fd\5\67\34"+
		"\2\u00fdf\3\2\2\2\u00fe\u00ff\5+\26\2\u00ff\u0100\5;\36\2\u0100\u0101"+
		"\5)\25\2\u0101\u0102\5G$\2\u0102\u0103\5\67\34\2\u0103h\3\2\2\2\u0104"+
		"\u0108\t\34\2\2\u0105\u0107\t\35\2\2\u0106\u0105\3\2\2\2\u0107\u010a\3"+
		"\2\2\2\u0108\u0106\3\2\2\2\u0108\u0109\3\2\2\2\u0109j\3\2\2\2\u010a\u0108"+
		"\3\2\2\2\u010b\u010d\7&\2\2\u010c\u010b\3\2\2\2\u010c\u010d\3\2\2\2\u010d"+
		"\u010f\3\2\2\2\u010e\u0110\t\36\2\2\u010f\u010e\3\2\2\2\u0110\u0111\3"+
		"\2\2\2\u0111\u010f\3\2\2\2\u0111\u0112\3\2\2\2\u0112l\3\2\2\2\u0113\u0117"+
		"\7=\2\2\u0114\u0116\n\37\2\2\u0115\u0114\3\2\2\2\u0116\u0119\3\2\2\2\u0117"+
		"\u0115\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u011a\3\2\2\2\u0119\u0117\3\2"+
		"\2\2\u011a\u011b\b\67\2\2\u011bn\3\2\2\2\u011c\u0120\7$\2\2\u011d\u011f"+
		"\n \2\2\u011e\u011d\3\2\2\2\u011f\u0122\3\2\2\2\u0120\u011e\3\2\2\2\u0120"+
		"\u0121\3\2\2\2\u0121\u0123\3\2\2\2\u0122\u0120\3\2\2\2\u0123\u0124\7$"+
		"\2\2\u0124p\3\2\2\2\u0125\u0127\7\17\2\2\u0126\u0125\3\2\2\2\u0126\u0127"+
		"\3\2\2\2\u0127\u0128\3\2\2\2\u0128\u0129\7\f\2\2\u0129r\3\2\2\2\u012a"+
		"\u012b\t!\2\2\u012b\u012c\3\2\2\2\u012c\u012d\b:\2\2\u012dt\3\2\2\2\t"+
		"\2\u0108\u010c\u0111\u0117\u0120\u0126\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}