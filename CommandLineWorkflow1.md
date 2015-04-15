# Introduction #

Like many Java applications, PROMNeT++ is distributed, in binary form, as a JAR file. This Wiki page shall serve as a guide to perform a PROMELA to C++ translation via the command line, under a Windows environment. For most modern operating systems (such as recent Linux distributions or OS X), the procedure detailed below should (hopefully) be almost the same, with minor variants.

# Prerequisites for this Workflow #

  * OMNeT++ must be installed.
    * For this guide, OMNeT++ is installed under _C:\omnetpp-4.3_
  * OMNeT++ must be fully functional, meaning it must be possible to launch its IDE via the standard _omnetpp_ command. Furthermore, it must be possible to run simulations, preferrably using OMNeT++'s graphical simulation environment (Tkenv).
    * We will resort to both OMNeT++'s IDE and graphical simulation environment later on.
    * Most samples that come with OMNeT++'s distribution should suffice when determining whether or not is possible to perform a graphical simulation.
  * PROMNeT++ (its JAR file and any other files that come with its standard distribution) must have been extracted to a directory of your choice; this guide will assume _C:\promnetpp_ as said directory. The screenshot below illustrates the contents of PROMNeT++'s standard distribution.<br><br><img src='https://promnetpp.googlecode.com/svn/media/Distribution1.png' /></li></ul>

# Steps #
## Phase 1 - Performing the translation ##

  1. Navigate to your OMNeT++ workspace directory. By default, assuming OMNeT++ is installed under _C:\omnetpp-4.3_, said workspace directory is located under under _C:\omnetpp-4.3\samples_
  1. Create a new folder for your (soon to be) OMNeT++ project, in this case, _OneThirdRule_ <br><br><img src='https://promnetpp.googlecode.com/svn/media/CreateFolderOneThirdRule.png' />
<ol><li>Navigate to the folder you just created and copy the PROMELA model (typically, a file with a <i>.pml</i> extension) that is to be submitted for translation to it. For this guide, said model is <i>NewOneThirdRule.pml</i><br><br><img src='https://promnetpp.googlecode.com/svn/media/CopyPMLFile.png' />
</li><li>Open a command prompt/shell/terminal at your current location. In Windows Vista and above, this can be easily done by holding down either of the <i>Shift</i> keys and choosing the <i>Open command window here</i> option.<br>
</li><li>Type in (or copy and paste) the following commands:<br>
<ul><li>For Windows users:<br>
<pre><code>set PROMNETPP_HOME=C:\promnetpp<br>
java -enableassertions -jar "%PROMNETPP_HOME%\promnetpp.jar" NewOneThirdRule.pml<br>
</code></pre>
</li><li>For Linux/OS X users:<br>
<pre><code>export PROMNETPP_HOME=/Users/yourusername/promnetpp<br>
java -enableassertions -jar $PROMNETPP_HOME/promnetpp.jar NewOneThirdRule.pml<br>
</code></pre>
</li></ul></li><li>Make sure the translation process was successful, meaning that:<br>
<ul><li>No errors were reported.<br>
</li><li>Multiple header (<i>.h</i>) and source (<i>.cc</i>) files have been generated.<br>
</li><li>In case of failure, consult PROMNeT++'s log file (<i>promnetpp-log.xml</i>) to trace the source of the problem. If at all possible, correct the problem and try again.</li></ul></li></ol>

<h2>Phase 2 - Creating the project</h2>

<ol><li>Launch the OMNeT++ IDE, via the standard <i>omnetpp</i> command.<br>
</li><li>Select File|New|OMNeT++ Project...<br>
</li><li>Under the <i>Project name</i> field, type the name of your project, as per the name of the folder you created in step 2 of the previous phase (in this guide's case, <i>OneThirdRule</i>)<br><br><img src='https://promnetpp.googlecode.com/svn/media/NewOmnetppProjectOneThirdRule.png' />
</li><li>Proceed to the next page of the New OMNeT++ Project wizard, and choose <i>Empty project</i> as your template.<br><br><img src='https://promnetpp.googlecode.com/svn/media/CreateEmptyProject.png' />
</li><li>Finish the project's creation by clicking on the <i>Finish</i> button.<br>
<ul><li>Alternatively, should you have any need to alter the toolchains and/or configurations for your project proceed to the next pages of the wizard and make any changes you desire. I suspect most users do not need to do this, however.</li></ul></li></ol>

<h2>Phase 3 - Compiling and running</h2>

<ol><li>At this point, you will most likely notice that your newly-created project contains an error. This is to be expected, as the OMNeT++ IDE, by default, creates a <i>package.ned</i> file which contains a package declaration. PROMNeT++ makes no use of packages; as such, all you have to do is delete the <i>package.ned</i> file. You may do this by either highlighting the file and pressing <i>Delete</i> on your keyboard, or by right-clicking on the file and choosing <i>Delete</i>, as shown below.<br><br><img src='https://promnetpp.googlecode.com/svn/media/DeletePackageNED.png' />
</li><li>Compile your project by right-clicking on it and choosing <i>Build Project</i>.<br><br><img src='https://promnetpp.googlecode.com/svn/media/BuildProject.png' />
</li><li>Finally, run a graphical simulation by right-clicking on your project and selecting Run As|OMNeT++ Simulation<br><br>The simulation environment shall resemble the screenshot below:<br><img src='https://promnetpp.googlecode.com/svn/media/OneThirdRuleSimulationEnvironment.png' /></li></ol>

<h2>Final remarks</h2>

<ol><li>If your simulation environment does not resemble the screenshot above, and all nodes have a question mark (?) as an icon, then you most likely need to download and extract the PROMNeT++ icon pack. Head over to the <a href='https://code.google.com/p/promnetpp/downloads/list'><i>Downloads</i></a> page to obtain it. Make sure you follow the instructions contained in the <i>README.txt</i> file that comes with the icon pack.