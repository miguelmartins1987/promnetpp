# Introduction #

If you're a researcher (or perhaps my internship's supervisor ![https://promnetpp.googlecode.com/svn/media/smiley-face-md-16px.png](https://promnetpp.googlecode.com/svn/media/smiley-face-md-16px.png)), then you might not care about OMNeT++ at all, but are interested instead in either analyzing how PROMNeT++ generates code and/or want to reuse said code. This is the case for a number of researchers out there, who would like to run the code in real-time systems.

**This guide is pretty much the same as CommandLineWorkflowSansOmnet, but it's targetted specifically for OS X users (my internship's supervisor, in particular).**

# Prerequisites for this Workflow #

  * PROMNeT++ requires Java 6 or higher to function. Install a JRE on your OS X system, if none is present.
  * PROMNeT++ (its JAR file and any other files that come with its standard distribution) must have been extracted to a directory of your choice; this guide will assume _/Users/miguel/promnetpp_ as said directory.
    * If you haven't done so already, head over to the <a href='https://code.google.com/p/promnetpp/downloads/list'><i>Downloads</i></a> page and download the latest PROMNeT++ release (PROMNeT++ Alpha 3 as of this guide's time of writing).
    * The screenshot below illustrates the contents of PROMNeT++'s standard distribution.<br><br><img src='https://promnetpp.googlecode.com/svn/media/Distribution2.png' />
<ul><li><a href='http://spinroot.com/'>Spin</a> must have been compiled from source and installed to a directory of your choice. Refer to <a href='https://code.google.com/p/promnetpp/wiki/InstallSpinOSX'>this</a> guide for instructions on how to do this.</li></ul></li></ul>

# Steps #

  1. Assuming PROMNeT++ has been extracted to a directory of your choice, as per the above prerequisites, you (the user) must, first and foremost, edit the _default-configuration.xml_ file that's packaged with the distribution, by using a text editor. We'll be editing the option named "spinHome", so that PROMNeT++ knows where to find Spin (for verification purposes).
    * There should be a line in the configuration file similar to
```
<simpleOption name="spinHome" value="C:/spin" />
```
    * All that's required in this step is to edit the value of the "simpleOption". You need to make sure that it points to the directory where Spin is installed. For this guide, the Spin distribution, and Spin's executable, are located under _/Users/miguel/Spin_. Thus, the above line becomes:
```
<simpleOption name="spinHome" value="/Users/miguel/Spin" />
```
    * Remember to save any changes to the configuration file when done.
  1. Once you have completed the above step, you must then choose a directory for both your PROMELA model and your translated files. This can be any directory of your choice, _just as long as you have the adequate permissions to read from and write files to that directory_. We will refer to this directory as the **PROMNeT++ workspace**, or **workspace** for short. For this guide, the workspace is located under _/Users/miguel/my promnetpp workspace_<br><br><img src='https://promnetpp.googlecode.com/svn/media/MyPromnetppWorkspaceOSX.png' />
<ol><li>Copy your PROMELA model and the <i>default-configuration.xml</i> file that comes with PROMNeT++ to the workspace. We'll be using the <i>NewOneThirdRule.pml</i> model packaged with the distribution.<br><br><img src='https://promnetpp.googlecode.com/svn/media/MyPromnetppWorkspaceOSXcont.png' />
</li><li>Open a terminal, and type in (or copy and paste) the following commands:<br>
<pre><code>cd /Users/miguel/"my promnetpp workspace"/<br>
export PROMNETPP_HOME=/Users/miguel/promnetpp<br>
java -enableassertions -jar $PROMNETPP_HOME/promnetpp.jar NewOneThirdRule.pml<br>
</code></pre>
</li><li>Wait for the tool to finish its verification/translation procedure.<br>
</li><li>If no errors occurred, you should now be in possession of the generated source code files, as illustrated in the screenshot below.<br><br><img src='https://promnetpp.googlecode.com/svn/media/MyPromnetppWorkspaceFullOSX.png' /></li></ol>

<h1>Final remarks/Troubleshooting</h1>

<ul><li>If the verification/translation procedure did not complete without errors, paying close attention to PROMNeT++'s output should help you correct the problem(s). If not, PROMNeT++ also keeps a log file, named <i>promnetpp-log.xml</i>, which is also useful for troubleshooting purposes.<br>
</li><li><b>It is entirely up to you, the user, to determine how you will use the generated source code files.</b> Once you're in possession of them, you're free to look at, or even copy portions of the code for your own personal use.