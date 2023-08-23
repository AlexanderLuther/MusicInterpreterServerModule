jflex TrackDefinitionLexer.flex
cup -parser TrackParser -symbols TrackSymbol TrackDefinitionParser.cup

mv TrackLexer.java ../lexer/
mv TrackParser.java ../parser/
mv TrackSymbol.java ../parser/
