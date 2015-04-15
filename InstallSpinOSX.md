# Introduction #

Installing Spin under OS X is relatively simple, and necessary if you're a OS X user who wants PROMNeT++ to verify your model before translating it to C++ code.

# Prerequisites #

Working versions of _GCC_ and _make_ should be installed on your OS X system. That is, it should be possible to execute each of the following commands in a terminal without fail:

```
gcc --version
make --version
```

# Steps #

  1. Download the latest Spin distribution labelled _Full distribution, with sources_ from the <a href='http://spinroot.com/spin/Src/index.html'>official distribution page</a>. At this guide's time of writing, said distribution is an archive named _spin625.tar.gz_
  1. Extract the archive's contents to a directory of your choice. For this guide, we'll be using _/Users/miguel_ (my home folder), as the screenshot below illustrates.<br><br><img src='https://promnetpp.googlecode.com/svn/media/OSXHomeDirectoryWithSpin.png' />
<ol><li>Open a terminal, and navigate to <i>/Users/miguel/Spin</i>, as per the screenshot below. The full Spin distribution should contain Spin's source code under a <i>Src<x.y.z></i> folder. A <i>README.html</i> file should also be present. Spin also packages some samples under the <i>Samples</i> folder; <i>iSpin</i> is also included.<br><br><img src='https://promnetpp.googlecode.com/svn/media/OSXSpinDistributionTerminal.png' />
</li><li>Navigate to the <i>Src<x.y.z></i> folder and open the <i>makefile</i> with a text editor. Simply typing <i>open makefile</i> as a command should suffice for most users; by default, it should open the Makefile with TextEdit.<br>
</li><li>Since we're compiling under OS X, it is necessary to append <i>-DMAC</i> to the <i>CFLAGS</i> variable, as illustrated below.<br><br><img src='https://promnetpp.googlecode.com/svn/media/OSXSpinMakeFile.png' />
</li><li>Save your changes to the Makefile, return to the terminal, and type in the following commands:<br>
<pre><code>make<br>
cp spin ..<br>
</code></pre>
</li><li>If done correctly, and assuming <i>make</i> did not report any errors during compilation, your Spin folder should now contain Spin's executable.<br><br><img src='https://promnetpp.googlecode.com/svn/media/OSXSpinDirectoryWithExecutable.png' />