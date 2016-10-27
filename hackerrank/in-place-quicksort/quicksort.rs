use std::io::{self, BufRead};

fn printvec(v: &Vec<i32>) {
    for x in v {
        print!("{} ", x);
    }
    println!("");
}


fn quicksort(array: &mut Vec<i32>, lo: usize, hi: usize) {
    if lo < hi {
        let p = partition(array, lo, hi);
        printvec(array);
        quicksort(array, lo, p - 1);
        quicksort(array, p + 1, hi);
    }
}

fn partition(array: &mut Vec<i32>, lo: usize, hi: usize) -> usize {
    let pivot = array[hi];
    let mut i:usize = lo;
    
    for j in lo..hi {
        if array[j] <= pivot {
            if i != j {
                array.as_mut_slice().swap(i, j);
            }
            i += 1
        }
    }
    array.as_mut_slice().swap(i, hi);
    i
}


fn main() {
    let reader = io::stdin();

    let mut nstr = String::new();
    reader.read_line(&mut nstr).unwrap();
    let n = nstr.trim_right().parse::<usize>().unwrap();
    
    let mut numbers: Vec<i32> = 
        reader.lock()
              .lines().next().unwrap().unwrap()
              .split(' ').map(|s| s.trim())
              .filter(|s| !s.is_empty())
              .map(|s| s.parse().unwrap())
              .collect();
  
    quicksort(&mut numbers, 0, n-1);
}

