
## 快排的基本思想

快速排序（Quicksort）是对冒泡排序的一种改进。

通过一趟排序将要排序的数据分割成独立的两部分，其中一部分的所有数据都比另外一部分的所有数据都要小，然后再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列。

快排原理：
        在要排的数（比如数组A）中选择一个中心值key（比如A[0]），通过一趟排序将数组A分成两部分，其中以key为中心，key右边都比key大，key左边的都key小，然后对这两部分分别重复这个过程，直到整个有序。
        整个快排的过程就简化为了一趟排序的过程，然后递归调用就行了。
        一趟排序的方法：
1，定义i=0，j=A.lenght-1，i为第一个数的下标，j为最后一个数下标
2，从数组的最后一个数Aj从右往左找，找到第一小于key的数，记为Aj；
3，从数组的第一个数Ai 从左往右找，找到第一个大于key的数，记为Ai；
4，交换Ai 和Aj 
5，重复这个过程，直到 i=j
6，调整key的位置，把A[i] 和key交换


##scala 实现快排

``` scala
def qSort(list:List[Int]) : List[Int] = list match {

case Nil => Nil
case :: (pivot, t) => qSort(t.filter(_ <= pivot))++List(pivot)++ qSort(t.filter(_ >pivot))
}

```

## java实现快排
``` java 
public class QSort {

  public static void main(String[] args) {
    int[] a = {1, 2, 4,45, 5, 7, 4, 5, 3, 9, 0};
    System.out.println(Arrays.toString(a));
    quickSort(a);
    System.out.println(Arrays.toString(a));
  }

  private static void quickSort(int[] a) {

    if (a.length > 0) {
      quickSort(a, 0, a.length - 1);
    }

  }

  private static void quickSort(int[] a, int low, int high) {
    //1 递归算法的出口
    if (low > high) {
      return;
    }

    int i = low;
    int j = high;

    int key = low;

    // 完成一趟排序
    while (i < j) {
      // 从右向左查找第一个小于key的值
      while (i < j && a[j] > key) {
        j--;
      }
      // 从左到右查找第一个大于key的值
      while (i < j && a[i] <= key) {
        i++;
      }
      // 交换值
      if (i < j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
      }
    }

    //调整key的位置
    int p = a[i];
    a[i] = a[low];
    a[low] = p;
//  对key左边的呢排序
    quickSort(a, low, i - 1);
    // 对key右边的排序
    quickSort(a, i + 1, high);
  }

}
``` 
