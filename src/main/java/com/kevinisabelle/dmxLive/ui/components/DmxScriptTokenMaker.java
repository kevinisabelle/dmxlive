package com.kevinisabelle.dmxLive.ui.components;

import javax.swing.text.Segment;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;

/**
 * DmxScript highlighting logic for script editor.
 *
 * @author kisabelle
 */
public class DmxScriptTokenMaker extends AbstractTokenMaker {

	public static final int TOKEN_SECTION = 100;

	@Override
	public TokenMap getWordsToHighlight() {

		TokenMap tokenMap = new TokenMap();

		tokenMap.put("Dim", Token.RESERVED_WORD);
		tokenMap.put("Blink", Token.RESERVED_WORD);
		tokenMap.put("Fade", Token.RESERVED_WORD);
		tokenMap.put("Strobe", Token.RESERVED_WORD);
		tokenMap.put("Pulse", Token.RESERVED_WORD);
		tokenMap.put("Mode", Token.RESERVED_WORD);
		tokenMap.put("Riff", Token.RESERVED_WORD);

		tokenMap.put("end$", Token.FUNCTION);

		tokenMap.put("c=", Token.MARKUP_TAG_NAME);
		tokenMap.put("d=", Token.MARKUP_TAG_NAME);
		tokenMap.put("t=", Token.MARKUP_TAG_NAME);
		tokenMap.put("o=", Token.MARKUP_TAG_NAME);
		tokenMap.put("u=", Token.MARKUP_TAG_NAME);
		tokenMap.put("m=", Token.MARKUP_TAG_NAME);
		tokenMap.put("s=", Token.MARKUP_TAG_NAME);
		tokenMap.put("r=", Token.MARKUP_TAG_NAME);

		return tokenMap;
	}

	@Override
	public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {
		// This assumes all keywords, etc. were parsed as "identifiers."

		if (tokenType == Token.IDENTIFIER) {
			int value = wordsToHighlight.get(segment, start, end);
			if (value != -1) {
				tokenType = value;
			}
		}

		super.addToken(segment, start, end, tokenType, startOffset);
	}

	/**
	 * Returns a list of tokens representing the given text.
	 *
	 * @param text The text to break into tokens.
	 * @param startTokenType The token with which to start tokenizing.
	 * @param startOffset The offset at which the line of tokens begins.
	 * @return A linked list of tokens representing <code>text</code>.
	 */
	@Override
	public Token getTokenList(Segment text, int startTokenType, int startOffset) {

		resetTokenList();

		char[] array = text.array;
		int offset = text.offset;
		int count = text.count;
		int end = offset + count;

		// Token starting offsets are always of the form:
		// 'startOffset + (currentTokenStart-offset)', but since startOffset and
		// offset are constant, tokens' starting positions become:
		// 'newStartOffset+currentTokenStart'.
		int newStartOffset = startOffset - offset;

		int currentTokenStart = offset;
		int currentTokenType = Token.NULL;

		for (int i = offset; i < end; i++) {

			char c = array[i];

			switch (currentTokenType) {

				case Token.NULL:

					currentTokenStart = i;   // Starting a new token here.

					switch (c) {

						case ' ':
						case '\t':
							currentTokenType = Token.WHITESPACE;
							break;
						case '$':
							currentTokenType = TOKEN_SECTION;
							break;
							
						case '#':
							currentTokenType = Token.ANNOTATION;
							break;

						case '|':

							addToken(text, i, i, Token.SEPARATOR, newStartOffset + i);
							currentTokenType = Token.NULL;
							break;

						case '/':

							currentTokenType = Token.COMMENT_EOL;
							break;

						default:

							// Anything not currently handled - mark as an identifier
							currentTokenType = Token.IDENTIFIER;
							break;

					} // End of switch (c).

					break;

				case Token.WHITESPACE:

					switch (c) {

						case ' ':
						case '\t':
							break;   // Still whitespace.

						case '|':
						case ':':
						case ',':

							addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
							currentTokenStart = i;

							addToken(text, i, i, Token.SEPARATOR, newStartOffset + i);
							currentTokenType = Token.NULL;
							break;

						default:   // Add the whitespace token and start anew.

							addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
							currentTokenStart = i;

							// Anything not currently handled - mark as identifier
							currentTokenType = Token.IDENTIFIER;

					} // End of switch (c).

					break;

				default: // Should never happen


				case TOKEN_SECTION:

					switch (c) {
						case ' ':
						case '\t':
							addToken(text, currentTokenStart, i - 1, Token.FUNCTION, newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = Token.WHITESPACE;
							break;

						case '|':
						case ':':
						case ',':

							addToken(text, currentTokenStart, i - 1, Token.FUNCTION, newStartOffset + currentTokenStart);
							currentTokenStart = i;

							addToken(text, i, i, Token.SEPARATOR, newStartOffset + i);
							currentTokenType = Token.NULL;

							break;

						default:
							break;
					}

					break;

				case Token.IDENTIFIER:

					switch (c) {

						case ' ':
						case '\t':
							addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = Token.WHITESPACE;
							break;

						case '|':
						case ':':
						case ',':

							addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);

							addToken(text, i, i, Token.SEPARATOR, newStartOffset + i);
							currentTokenType = Token.NULL;
							break;

						case '=':

							addToken(text, currentTokenStart, i, Token.IDENTIFIER, newStartOffset + currentTokenStart);

							currentTokenType = Token.NULL;
							break;

						default:

							if (RSyntaxUtilities.isLetterOrDigit(c) || c == '_') {
								break;   // Still an identifier of some type.
							}
						// Otherwise, we're still an identifier (?).

					} // End of switch (c).

					break;

				case Token.COMMENT_EOL:

					i = end - 1;
					addToken(text, currentTokenStart, i, currentTokenType, newStartOffset + currentTokenStart);
					// We need to set token type to null so at the bottom we don't add one more token.
					currentTokenType = Token.NULL;
					break;
					
				case Token.ANNOTATION:

					i = end - 1;
					addToken(text, currentTokenStart, i, currentTokenType, newStartOffset + currentTokenStart);
					// We need to set token type to null so at the bottom we don't add one more token.
					currentTokenType = Token.NULL;
					break;

			} // End of switch (currentTokenType).

		} // End of for (int i=offset; i<end; i++).

		//addNullToken();

		switch (currentTokenType) {

			// Do nothing if everything was okay.
			case Token.NULL:
				addNullToken();
				break;

			case TOKEN_SECTION:

				addToken(text, currentTokenStart, end - 1, Token.FUNCTION, newStartOffset + currentTokenStart);
				break;

			// All other token types don't continue to the next line...
			default:
				addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);

		}

		// Return the first token in our linked list.
		return firstToken;

	}
}
