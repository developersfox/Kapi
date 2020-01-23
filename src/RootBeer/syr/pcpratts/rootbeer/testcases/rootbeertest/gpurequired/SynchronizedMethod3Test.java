/*
 * This file is part of Rootbeer.
 * 
 * Rootbeer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Rootbeer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Rootbeer.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package RootBeer.syr.pcpratts.rootbeer.testcases.rootbeertest.gpurequired;

import RootBeer.syr.pcpratts.rootbeer.runtime.Kernel;
import RootBeer.syr.pcpratts.rootbeer.test.TestSerialization;
import java.util.ArrayList;
import java.util.List;

public class SynchronizedMethod3Test implements TestSerialization {

  @Override
  public List<Kernel> create() {
    List<Kernel> jobs = new ArrayList<Kernel>();
    SynchronizedMethod3Object obj = new SynchronizedMethod3Object();
    for(int i = 0; i < 4096; ++i){
      SynchronizedMethod3RunOnGpu curr = new SynchronizedMethod3RunOnGpu(obj);
      jobs.add(curr);
    }
    return jobs;
  }

  @Override
  public boolean compare(Kernel rog, Kernel rog1) {
    SynchronizedMethod3RunOnGpu lhs = (SynchronizedMethod3RunOnGpu) rog;
    SynchronizedMethod3RunOnGpu rhs = (SynchronizedMethod3RunOnGpu) rog1;
    return lhs.compare(rhs);
  }

  
}