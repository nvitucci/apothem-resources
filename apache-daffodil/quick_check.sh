#!/bin/bash

DAFFODIL_HOME=$1
VERSION=$2

$DAFFODIL_HOME/bin/daffodil parse -s $VERSION/bsc.dfdl.xsd bsc5_padded.dat > /tmp/complete_$VERSION
$DAFFODIL_HOME/bin/daffodil unparse -s $VERSION/bsc.dfdl.xsd /tmp/complete_$VERSION > /tmp/complete_unparsed_$VERSION

diff -q bsc5_padded.dat /tmp/complete_unparsed_$VERSION
