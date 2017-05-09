use std::io::stdin;
use std::vec::Vec;

type Matrix = Vec<Vec<i32>>;
type MatrixPos = Vec<Vec<(usize, usize)>>;

fn read_params() -> (usize,usize,i32){
    let mut buffer = String::new();

    stdin().read_line(&mut buffer).expect("Expected M,N,R");

    let mut iter = buffer.as_str()
        .split_whitespace()
        .map(|a| a.parse::<i32>().unwrap())
    ;

    (iter.next().unwrap_or(0) as usize,
     iter.next().unwrap_or(0) as usize,
     iter.next().unwrap_or(0))
}

fn read_matrix(m: usize) -> Matrix {
    let mut vector = Vec::new();
    for i in 0..m {
        let mut buffer = String::new();
        stdin().read_line(&mut buffer).expect(format!("Expected line {}", i).as_ref());

        vector.push(buffer.as_str()
            .split_whitespace()
            .map(|a| a.parse::<i32>().unwrap())
            .collect()
        )
    }

    vector
}

fn linearize(m: usize, n: usize, i:usize, j:usize) -> MatrixPos {

    fn snake(m: usize, n: usize, i:usize, j:usize) -> Vec<(usize,usize)> {
        (0..n).map(|c| (i, j + c))
            .chain((1..m).map(|r| (i + r, j + n - 1)))
            .chain((0..n-1).rev().map(|c| (i + m - 1, j + c)))
            .chain((1..m-1).rev().map(|r| (i + r, j)))
            .collect()
    }

    if m < 2 || n < 2 {
        return MatrixPos::new()
    }

    let mut recsnakes = linearize(m - 2, n -2, i+1, j+1);
    recsnakes.push(snake(m, n,i,j));

    return recsnakes
}

fn delinearize(m: usize, n: usize, snakes: &MatrixPos) -> MatrixPos {
    let mut array: MatrixPos = vec![vec![(0, 0); n]; m];

    let mut nest = snakes.len() - 1;
    for snake in snakes {
        let (tr, bl) = snake.split_at(n - 2 * nest + (m - 2 * nest - 2));

        let (top, right) = tr.split_at(n - 2*nest);
        let (bottom, left) = bl.split_at(n - 2*nest);

        for k in 0..top.len() {
            array[nest][nest + k] = top[k]
        }
        for k in 0..right.len() {
            array[nest + 1 + k][n - 1 - nest] = right[k]
        }
        for k in 0..bottom.len() {
            array[m - 1 - nest][n - 1 - nest - k] = bottom[k]
        }
        for k in 0..left.len() {
            array[m - 2 - nest - k][nest] = left[k]
        }

        if nest > 0 {
            nest -= 1;
        }
    }

    array
}

fn rotate(r : &i32, linearized : &MatrixPos) -> MatrixPos {
    let mut rotated : MatrixPos = MatrixPos::new();

    for snake in linearized {
        let rotations = (r % snake.len() as i32) as usize;
        let rsnake = snake.iter()
            .skip(rotations)
            .chain(snake.iter().take(rotations))
            .cloned()
            .collect();

        rotated.push(rsnake)
    }

    rotated
}

fn print_result(m: usize, n:usize, rot_array: &MatrixPos, array: &Matrix) {
    for i in 0..m {
        for j in 0..n {
            let (r,c) = rot_array[i as usize][j as usize];
            print!("{} ", array[r][c])
        }
        println!();
    }
}

fn main() {
    let (m,n,r) = read_params();
    let array = read_matrix(m);

    let linearized = linearize(m as usize, n as usize, 0, 0);
    let rotated = rotate(&r, &linearized);
    let rot_array = delinearize(m, n, &rotated);

    print_result(m, n, &rot_array, &array)
}


#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_linearize_3x3() {
        assert_eq!(
        linearize(3,3,0,0),
        vec![vec![(0,0),(0,1),(0,2),(1,2),(2,2),(2,1),(2,0),(1,0)]]
        );
    }

    #[test]
    fn test_linearize_4x4() {
        assert_eq!(
        linearize(4,4,0,0),
        vec![
            vec![(1,1),(1,2),(2,2),(2,1)],
            vec![(0,0),(0,1),(0,2),(0,3),(1,3),(2,3),(3,3),(3,2),(3,1),(3,0),(2,0),(1,0)]
        ]
        );
    }

    #[test]
    fn test_linearize_4x5() {
        assert_eq!(
        linearize(4,5,0,0),
        vec![
            vec![(1,1),(1,2),(1,3),(2,3),(2,2),(2,1)],
            vec![(0,0),(0,1),(0,2),(0,3),(0,4),(1,4),(2,4),(3,4),(3,3),(3,2),(3,1),(3,0),(2,0),(1,0)]
        ]
        );
    }

    #[test]
    fn test_linearize_5x4() {
        assert_eq!(
        linearize(5,4,0,0),
        vec![
            vec![(1,1),(1,2),(2,2),(3,2),(3,1),(2,1)],
            vec![(0,0),(0,1),(0,2),(0,3),(1,3),(2,3),(3,3),(4,3),(4,2),(4,1),(4,0),(3,0),(2,0),(1,0)]
        ]
        );
    }

    #[test]
    fn test_delinearize_3x3() {
        let tmp = vec![vec![(0,0),(0,1),(0,2),(1,2),(2,2),(2,1),(2,0),(1,0)]];
        assert_eq!(
            delinearize(3,3,&tmp),
            vec![
                vec![(0,0),(0,1),(0,2)],
                vec![(1,0),(0,0),(1,2)],
                vec![(2,0),(2,1),(2,2)]
            ]
        );
    }

    #[test]
    fn test_delinearize_4x4() {
        let tmp = vec![
            vec![(1,1),(1,2),(2,2),(2,1)],
            vec![(0,0),(0,1),(0,2),(0,3),(1,3),(2,3),(3,3),(3,2),(3,1),(3,0),(2,0),(1,0)]
        ];

        assert_eq!(
        delinearize(4,4,&tmp),
        vec![
            vec![(0,0),(0,1),(0,2),(0,3)],
            vec![(1,0),(1,1),(1,2),(1,3)],
            vec![(2,0),(2,1),(2,2),(2,3)],
            vec![(3,0),(3,1),(3,2),(3,3)]
        ]
        );
    }

    #[test]
    fn test_delinearize_5x4() {
        let tmp = vec![
            vec![(1,1),(1,2),(2,2),(3,2),(3,1),(2,1)],
            vec![(0,0),(0,1),(0,2),(0,3),(1,3),(2,3),(3,3),(4,3),(4,2),(4,1),(4,0),(3,0),(2,0),(1,0)]
        ];

        assert_eq!(
        delinearize(5,4,&tmp),
        vec![
            vec![(0,0),(0,1),(0,2),(0,3)],
            vec![(1,0),(1,1),(1,2),(1,3)],
            vec![(2,0),(2,1),(2,2),(2,3)],
            vec![(3,0),(3,1),(3,2),(3,3)],
            vec![(4,0),(4,1),(4,2),(4,3)]
        ]
        );
    }


    #[test]
    fn test_no_rotation() {
        let linearized = linearize(3,3,0,0);
        let rotated = rotate(&0, &linearized);
        let rot_array = delinearize(3,3,&rotated);

        assert_eq!(rot_array[0][0], (0,0));
        assert_eq!(rot_array[0][1], (0,1));
        assert_eq!(rot_array[0][2], (0,2));
        assert_eq!(rot_array[1][0], (1,0));
       // assert_eq!(rot_array[1][1], (1,1));
        assert_eq!(rot_array[1][2], (1,2));
        assert_eq!(rot_array[2][0], (2,0));
        assert_eq!(rot_array[2][1], (2,1));
        assert_eq!(rot_array[2][2], (2,2));
    }

    #[test]
    fn test_4dim_rot7() {
        let linearized = linearize(5,4,0,0);
        let rotated = rotate(&7, &linearized);
        let rot_array = delinearize(5,4,&rotated);

        assert_eq!(rot_array[0][0], (4,3));
        assert_eq!(rot_array[0][1], (4,2));
        assert_eq!(rot_array[0][2], (4,1));
        assert_eq!(rot_array[0][3], (4,0));
        assert_eq!(rot_array[1][3], (3,0));
        assert_eq!(rot_array[2][3], (2,0));
        assert_eq!(rot_array[3][3], (1,0));
        assert_eq!(rot_array[4][3], (0,0));
        assert_eq!(rot_array[4][2], (0,1));
        assert_eq!(rot_array[4][1], (0,2));
        assert_eq!(rot_array[4][0], (0,3));
        assert_eq!(rot_array[3][0], (1,3));
        assert_eq!(rot_array[2][0], (2,3));
        assert_eq!(rot_array[1][0], (3,3));
        assert_eq!(rot_array[1][1], (1,2));
        assert_eq!(rot_array[1][2], (2,2));
        assert_eq!(rot_array[2][2], (3,2));
        assert_eq!(rot_array[2][1], (1,1));
    }

//
//    5 4 7

//     1  2  3  4
//     7  8  9 10
//    13 14 15 16
//    19 20 21 22
//    25 26 27 28
//
//    Your Output (stdout)
//
//    28 27 26 25
//     1  9 15 19
//    22  1 21 13
//    16  8 20  7
//    10  3  2  1
//    Expected Output

//    28 27 26 25
//22 9 15 19
//16 8 21 13
//10 14 20 7
//4 3 2 1

}