# Visgraph generator by Team Griffin

from __future__ import print_function
import pyvisgraph as vg
import itertools
import time
import sys

if (len(sys.argv) > 1):
    instance_number = int(sys.argv[1])
    # Import instance information
    execfile("instance_%d.py" % instance_number)
    
    # Build visibility graph.
    t0 = time.clock()
    print("Building visgraph for instance %d." % instance_number)
    g = vg.VisGraph()
    g.build(polys, status = True)
    g.save('graph_%d.pk1' % instance_number)
    print("Building visgraph took %f seconds." % (time.clock()-t0))
    
else:
    print("Invalid instance number")