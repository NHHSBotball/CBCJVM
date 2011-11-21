#/bin/bash


#####
# 0. Set up build environment
#####

DEV="$HOME/dev"
INSTALL="/mnt/user"
sudo mkdir -p $INSTALL
sudo chown -R $USER:$USER $INSTALL

INSTALL_SOFTWARE=yes
INSTALL_TOOLCHAIN=no
INSTALL_CLASSPATH=yes
INSTALL_ZLIB=yes
INSTALL_JAMVM=yes


#####
# 1. Install essential software
#####

if [ "$INSTALL_SOFTWARE" = yes ]; then

# software for building

sudo apt-get -y install build-essential pkg-config

# a java compiler and a jar utility; either of these setups should work

sudo apt-get -y install fastjar ecj
#sudo apt-get -y install gcj
#sudo apt-get -y install openjdk-6-jdk

fi


#####
# 2. Install the GCC ARM toolchain
#####

if [ "$INSTALL_TOOLCHAIN" = yes ]; then

#if you had a previous toolchain installed, rename any existing /usr/arm-linux dir and remove any link named /usr/arm-linux 

[ -d /usr/arm-linux ] && sudo mv /usr/arm-linux /usr/arm-linux-4.1.2
[ -L /usr/arm-linux ] && sudo rm -f /usr/arm-linux

#download the toolchain 
TOOLCHAIN_DIR="arm-2008q3"
TOOLCHAIN_FILE="$TOOLCHAIN_DIR-72-arm-none-linux-gnueabi-i686-pc-linux-gnu.tar.bz2"
TOOLCHAIN_DL="http://files.chumby.com/toolchain/$TOOLCHAIN_FILE"

mkdir -p $DEV/toolchain
cd $DEV/toolchain
[ ! -f $TOOLCHAIN_FILE ] && wget -O $TOOLCHAIN_FILE $TOOLCHAIN_DL

#extract and create directory symlink 

tar xjvf $TOOLCHAIN_FILE
sudo mv $TOOLCHAIN_DIR /usr
sudo ln -s /usr/$TOOLCHAIN_DIR /usr/arm-linux

#move any old gcc files out of /usr/bin 

cd /usr/bin
sudo mkdir arm-linux-4.1.2
sudo mv arm-linux-[a-z]* arm-linux-4.1.2/

#set up new symlinks in /usr/arm-linux/bin 

cd /usr/arm-linux/bin
for i in *; do sudo ln -s $i `echo $i|awk -F"-" '{print $NF;}'`; done
sudo rm -f 4.3.2
sudo ln -s arm-none-linux-gnueabi-gcc-4.3.2 gcc-4.3.2

#set up new symlinks in /usr/bin 

for i in `ls /usr/arm-2008q3/bin/arm-none-linux-gnueabi-*`; do name=`echo $i|awk -F"-" {'print $NF'}`; sudo ln -s $i /usr/bin/arm-linux-$name; done
sudo rm /usr/bin/arm-linux-4.3.2
sudo ln -s /usr/$TOOLCHAIN_DIR/bin/arm-none-linux-gnueabi-gcc-4.3.2 /usr/bin/arm-linux-gcc-4.3.2

fi


#####
# 3. Build GNU Classpath
#####

if [ "$INSTALL_CLASSPATH" = yes ]; then

#download and extract Classpath 
CLASSPATH_DIR="classpath-0.98"
CLASSPATH_FILE="$CLASSPATH_DIR.tar.gz"
CLASSPATH_DL="ftp://ftp.gnu.org/gnu/classpath/$CLASSPATH_FILE"

mkdir -p $DEV/classpath
cd $DEV/classpath
[ ! -f $CLASSPATH_FILE ] && wget -O $CLASSPATH_FILE $CLASSPATH_DL
[ -d $CLASSPATH_DIR ] && sudo rm -r $CLASSPATH_DIR
tar xvf $CLASSPATH_FILE

#build classpath

cd $CLASSPATH_DIR
./configure\
  --disable-gtk-peer --disable-gconf-peer --disable-plugin\
  --prefix=$DEV/classpath/install\
  --host=arm-linux --target=arm-linux
#TODO do this somehow with ./configure
find . -name Makefile\* -exec \
  sed -i 's/@WARNING_CFLAGS@ @STRICT_WARNING_CFLAGS@ @ERROR_CFLAGS@/@WARNING_CFLAGS@/' '{}' \;
make
make install

fi


#####
# 4. Build zlib
#####

if [ "$INSTALL_ZLIB" = yes ]; then

#download and extract zlib 
ZLIB_DIR="zlib-1.2.5.tar.bz2"
ZLIB_FILE="$ZLIB_DIR.tar.bz2"
ZLIB_DL="http://zlib.net/$ZLIB_FILE"

mkdir -p $DEV/zlib
cd $DEV/zlib
[ ! -f $ZLIB_FILE ] && wget -O $ZLIB_FILE $ZLIB_DL
[ -d $ZLIB_DIR ] && sudo rm -r $ZLIB_DIR
tar xjvf $ZLIB_FILE

#build zlib

cd $ZLIB_DIR
PATH_BAK=$PATH
PATH=/usr/arm-linux/bin:$PATH_BAK
./configure\
  --prefix=$DEV/zlib/install
make
make install
PATH=$PATH_BAK

fi


#####
# 5. Prepare paths for building JamVM
#####

if [ "$INSTALL_JAMVM" = yes ]; then

mkdir -p $INSTALL/lib
mkdir -p $INSTALL/include
mkdir -p $INSTALL/jvm
cd $INSTALL

cp $DEV/classpath/install/lib/classpath/*.so.?.?.? lib
cp $DEV/classpath/install/share/classpath/*.zip lib
cp $DEV/classpath/install/lib/logging.properties lib
cp $DEV/zlib/install/lib/*.so.?.?.? lib
cp $DEV/classpath/install/include/*.h include
cp $DEV/zlib/install/include/*.h include

for FILE in lib/*.so.?.?.?; do
  mv "$FILE" `echo "$FILE" | sed 's/\(\.[0-9]\)\{3\}//'`
done

#####
# 6. Build JamVM
#####

#download and extract JamVM 
JAMVM_DIR="jamvm-1.5.4"
JAMVM_FILE="$JAMVM_DIR.tar.gz"
JAMVM_DL="http://downloads.sourceforge.net/project/jamvm/jamvm/JamVM%201.5.4/jamvm-1.5.4.tar.gz?r=http%3A%2F%2Fsourceforge.net%2Fprojects%2Fjamvm"

mkdir -p $DEV/jamvm
cd $DEV/jamvm
[ ! -f $JAMVM_FILE ] && wget -O $JAMVM_FILE $JAMVM_DL
[ -d $JAMVM_DIR ] && sudo rm -r $JAMVM_DIR
tar xvf $JAMVM_FILE

#build JamVM
#TODO
cd $JAMVM_DIR
./configure\
  --with-classpath-install-dir=$DEV/classpath/install\
  --prefix=$DEV/jamvm/install\
  --host=arm-linux --target=arm-linux\
  LDFLAGS=-L$INSTALL/lib CPPFLAGS=-I$INSTALL/include
make
make install

fi


#####
# 7. Build CBCJVM deployment structure
#####

cd $INSTALL

cp $DEV/jamvm/install/lib/*.so.?.?.? lib
cp $DEV/jamvm/install/share/jamvm/*.zip lib
cp $DEV/jamvm/install/bin/* jvm

for FILE in lib/*.so.?.?.?; do
  mv "$FILE" `echo "$FILE" | sed 's/\(\.[0-9]\)\{3\}//'`
done

