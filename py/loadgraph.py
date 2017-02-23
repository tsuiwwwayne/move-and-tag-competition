# Path generator by Team Griffin

from __future__ import print_function

import pyvisgraph as vg

import itertools
import time
import sys

if (len(sys.argv) > 1):
    instance_number = int(sys.argv[1])
    # Import instance information
    execfile("instance_%d.py" % instance_number)
    
    # Calculate shortest paths

    g = vg.VisGraph()
    g.load('graph_%d.pk1' % instance_number)

    # Compute paths

    f = open('C:\\dev\\path_%d.txt' % instance_number, 'w')

    t1 = time.clock()

    loopCounter = 0
    totalCount = len(robots)
    for pair in itertools.combinations(robots, 2):
        #print("From: %d to %d:" % (pair[0].extradata_1, pair[1].extradata_1))
        shortest = g.shortest_path(pair[0], pair[1])
        numWaypoints = len(shortest) - 2
        #print("Waypoint length: %d" % numWaypoints)
        
        if numWaypoints > 0:
            #print(shortest)
            builtstr = "%d,%d#" % (pair[0].extradata_1, pair[1].extradata_1)
            # First element
            builtstr += str(shortest[1].extradata_1)
            builtstr += ":"
            builtstr += str(shortest[1].extradata_2)
            
            # All the rest
            for waypoint in shortest[2:-1]:
                builtstr += "|"
                builtstr += str(waypoint.extradata_1)
                builtstr += ":"
                builtstr += str(waypoint.extradata_2)
                
            print(builtstr, file=f)
        
        loopCounter += 1
        if (loopCounter % 20 == 0):
            elapsed = time.clock() - t1
            completed = pair[0].extradata_1
            left = totalCount - pair[0].extradata_1
            if completed > 0:
                time_for_one = elapsed / completed
            else:
                time_for_one = 0
                
            time_remaining = time_for_one * left
            print("Status: %d:%d / %d. Elapsed: %f seconds. Estimated time remaining: %f s." % (completed, pair[1].extradata_1, totalCount, elapsed, time_remaining))

    print("Computing paths took %f seconds." % (time.clock()-t1))
    
else:
    print("Invalid instance number")