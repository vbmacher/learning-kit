use std::io::{self, BufRead};

fn print(v: &mut Vec<i32>) {
    println!("{}", v.iter().fold(String::new(), |res, s| {
        if res.is_empty() {
            return s.to_string();
        } else {
            return res + " " + &s.to_string()
        }
    }));
}

fn quicksort(v: &mut Vec<i32>, lo:usize, hi:usize) {
    if lo < hi {
        let p = partition(v, lo, hi);
        print(v);
        if p > 0 {
            quicksort(v, lo, p - 1);
        }
        quicksort(v, p + 1, hi);
    }
}


fn partition(v: &mut Vec<i32>, lo: usize, hi: usize) -> usize {
    let pivot = v[hi];
    let mut i = lo;
    
    for j in lo..hi {
        if v[j] <= pivot {
            if i != j {
                v.as_mut_slice().swap(i,j);
            }
            i += 1;
        }
    }
    v.as_mut_slice().swap(i,hi);
    return i
}



fn main() {
    let reader = io::stdin();
    let mut buffer = String::new();
    reader.read_line(&mut buffer).unwrap();
    let n:usize = buffer.trim_right().parse().unwrap();
    
    if n <=0 {
        return;
    }
    
    let mut numbers: Vec<i32> = reader.lock().lines().next()
        .unwrap().unwrap()
        .split(' ')
        .map(|s| s.trim())
        .filter(|s| !s.is_empty())
        .map(|s| s.parse().unwrap())
        .collect();
 
    quicksort(&mut numbers, 0, n -1);  
}
