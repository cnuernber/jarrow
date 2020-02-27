# Basic makefile for jarrow.
# Can autogenerate flatbuffer java source files if flatc is present.
# Otherwise, it just compiles the source files and builds javadocs.
# Some paths may need changing

JAVA_HOME = /mbt/local/java/jdk1.6.0_41/bin
JAVAC = $(JAVA_HOME)/javac
JAVADOC = $(JAVA_HOME)/javadoc
VERSION = 1.0

SRCDIR = java/uk/ac/bristol/star
FBSDIR = $(SRCDIR)/fbs

PYTHON_DIR = /mbt/local/pkg/miniconda3/bin
PYTHON = $(PYTHON_DIR)/python3

FLATC = /mbt/github/flatbuffers/flatc

JARFILE = jarrow.jar

JSRC = \
       $(SRCDIR)/feather/AbstractColumnWriter.java \
       $(SRCDIR)/feather/BooleanRandomWriter.java \
       $(SRCDIR)/feather/Buf.java \
       $(SRCDIR)/feather/BufAccess.java \
       $(SRCDIR)/feather/BufMapper.java \
       $(SRCDIR)/feather/BufUtils.java \
       $(SRCDIR)/feather/ColStat.java \
       $(SRCDIR)/feather/Decoder.java \
       $(SRCDIR)/feather/FeatherColumn.java \
       $(SRCDIR)/feather/FeatherColumnWriter.java \
       $(SRCDIR)/feather/FeatherTable.java \
       $(SRCDIR)/feather/FeatherType.java \
       $(SRCDIR)/feather/FeatherWriter.java \
       $(SRCDIR)/feather/NumberRandomWriter.java \
       $(SRCDIR)/feather/Reader.java \
       $(SRCDIR)/feather/VariableLengthRandomWriter.java \

FBSRC = \
       $(FBSDIR)/google/ByteBufferUtil.java \
       $(FBSDIR)/google/Constants.java \
       $(FBSDIR)/google/FlatBufferBuilder.java \
       $(FBSDIR)/google/Struct.java \
       $(FBSDIR)/google/Table.java \
       $(FBSDIR)/google/Utf8.java \
       $(FBSDIR)/google/Utf8Safe.java \

FBFEATHERSRC = \
       $(FBSDIR)/feather/CategoryMetadata.java \
       $(FBSDIR)/feather/Column.java \
       $(FBSDIR)/feather/CTable.java \
       $(FBSDIR)/feather/DateMetadata.java \
       $(FBSDIR)/feather/Encoding.java \
       $(FBSDIR)/feather/PrimitiveArray.java \
       $(FBSDIR)/feather/TimeMetadata.java \
       $(FBSDIR)/feather/TimestampMetadata.java \
       $(FBSDIR)/feather/TimeUnit.java \
       $(FBSDIR)/feather/Type.java \
       $(FBSDIR)/feather/TypeMetadata.java \

build: $(JARFILE) javadocs

read: $(JARFILE) test/data.fea
	java -ea -classpath $(JARFILE) \
             uk.ac.bristol.star.feather.FeatherTable test/data.fea

write: test/test.fea

rw: test/test.fea
	java -ea -classpath $(JARFILE) \
             uk.ac.bristol.star.feather.FeatherTable test/test.fea

test/test.fea: $(JARFILE)
	java -ea -classpath $(JARFILE) \
             uk.ac.bristol.star.feather.FeatherWriter >$@

jar: $(JARFILE)

javadocs: $(JSRC) 
	rm -rf javadocs
	mkdir javadocs
	$(JAVADOC) -quiet \
                -classpath $(STIL_JAR):$(JSON_JAR) \
                -d javadocs \
                $(JSRC) $(FBSRC) $(FBFEATHERSRC)

test/data.fea: test/data.py
	$(PYTHON) data.py

test/big.fea: test/big.py
	$(PYTHON) big.py

$(FBSRC) $(FBFEATHERSRC):
	rm -rf $(FBSDIR)
	mkdir -p $(FBSDIR)
	cd java; \
        $(FLATC) --java ../feather.fbs

$(JARFILE): $(JSRC) $(FBSRC) $(FBFEATHERSRC)
	rm -rf tmp
	mkdir -p tmp
	$(JAVAC) -Xlint:all,-serial,-path -d tmp \
              $(JSRC) $(FBSRC) $(FBFEATHERSRC) \
            && echo $(VERSION) > tmp/uk/ac/bristol/star/feather/jarrow.version \
            && jar cf $@ -C tmp .
	rm -rf tmp

clean:
	rm -f $(JARFILE)
	rm -f test.fea big.fea
	rm -rf tmp javadocs

veryclean: clean
	rm -f test/data.fea
	rm -rf $(FBSDIR)


