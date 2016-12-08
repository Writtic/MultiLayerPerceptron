public class MultiLayerPerceptron {
    public static void main(String args[]) {
        int N = 2;          // 두 개의 입력 뉴런
        int H = 2;          // 두 개의 은닉 뉴런
        int M = 1;          // 한 개의 출력 뉴런
        double eta = 0.2;   // 학습률의 설정

        double E  = 100;    // 아래 while문 안으로 진입하도록 비용함수를 큰 값으로 초기화
        double Es = 0.01;   // 프로그램을 정지시킬 때 기준이 되는 비용 함수
        int count = 0;      // 반복학습 횟수 계수기의 초기화



        double w_ji[][] = new double[2][3];
        double w_kj[][] = new double[1][3];
        // I-2 단계
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                w_ji[i][j] = (Math.random()) - 0.5;
            }
        }
        // 연결 가중치와 임계치를 -0.5 ~ 0.5 의 임의 실수값으로 설정
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                w_kj[i][j] = (Math.random()) - 0.5;
            }
        }

        double net_pj[] = new double[H];
        double o_pj[]   = new double[H + 1];
        double net_pk[] = new double[M];
        double o_pk[]   = new double[M];
        double E_p[]    = new double[4];
        double delta_pk[] = new double[M];
        double delta_pj[] = new double[H];

        while (E > Es) {

            count += 1;
            E = 0;          // 비용 함수를 0으로 초기화
            // F-1 단계 : 입력과 출력값들 제시
            int[][] X = {{0, 0, -1}, {0, 1, -1}, {1, 0, -1}, {1, 1, -1}};   // 입력값
            int[] D = {0, 1, 1, 0};                                         // 목표값

            for (int p = 0; p < 4; p++) {
                for (int j = 0; j < H; j++) {
                    net_pj[j] = 0;
                    for (int i = 0; i < N + 1; i++) {
                        // F-2 단계 : 은닉 뉴론의 입력합
                        net_pj[j] = net_pj[j] + w_ji[j][i] * X[p][i];//X[p][i]
                    }
                    // F-3 단계 : 은닉 뉴론의 출력
                    o_pj[j] = 1 / (1 + Math.exp(-net_pj[j]));
                }
                o_pj[H] = -1;        // 출력 노드의 임계치를 배우기 위한 설정

                for (int k = 0; k < M; k++) {
                    net_pk[k] = 0;
                    for (int j = 0; j < H + 1; j++) {
                        // F-4 단계 : 출력 뉴론의 입력합
                        net_pk[k] = net_pk[k] + w_kj[k][j] * o_pj[j];
                    }
                    // F-4 단계 : 출력 뉴론의 실제 출력값
                    o_pk[k] = 1 / (1 + Math.exp(-net_pk[k]));
                }
                // 데이터 패턴별 비용함수 초기화
                E_p[p] = 0;
                // B-1 단계 : 출력 뉴론의 델타 계산
                for (int k = 0; k < M; k++) {
                    delta_pk[k] = (D[p] - o_pk[k]) * o_pk[k] * (1 - o_pk[k]);
                    E_p[p] = E_p[p] + 0.5 * Math.pow((D[p] - o_pk[k]), 2);
                }

                // 누적 비용 함수
                E = E + E_p[p];
                // B-2 단계 : 은닉 뉴론의 델타 계산
                for (int j = 0; j < H; j++) {
                    delta_pj[j] = 0;
                    for (int k = 0; k < M; k++) {
                        delta_pj[j] = delta_pj[j] + delta_pk[k] * w_kj[k][j] * o_pj[j] * (1 - o_pj[j]);
                    }
                }
                // B-3 단계 : 은닉층과 출력츠간의 연결 가중치 갱신
                for (int k = 0; k < M; k++) {
                    for (int j = 0; j < H + 1; j++) {
                        w_kj[k][j] = w_kj[k][j] + eta * delta_pk[k] * o_pj[j];
                    }
                }
                // B-4 단계 : 은닉층과 입력층간의 연결 가중치 갱신
                for (int j = 0; j < H; j++) {
                    for (int i = 0; i < N + 1; i++) {
                        w_ji[j][i] = w_ji[j][i] + eta * delta_pj[j] * X[p][i];
                    }
                }
            }
            System.out.println("Count : " + count + " / 오차의 변화 추이 : " + E);
        }
        System.out.println();
        System.out.println("XOR 문제 학습 결과");
        System.out.println("학습이 완료된 후 연결 가중치 확인을 위한 출력");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print("w" + i + "" + j + " = ");
                System.out.printf("%.2f", w_ji[i][j]);
                System.out.print(" | ");
            }
        }
        System.out.println();
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print("w" + i + "" + j + " = ");
                System.out.printf("%.2f", w_kj[i][j]);
                System.out.print(" | ");
            }
        }
        System.out.println("\n");
        int[][] X = {{0, 0, -1}, {0, 1, -1}, {1, 0, -1}, {1, 1, -1}};
        for (int i = 0; i < 4; i++) {
            System.out.print("X0=" + X[i][0] + " 와 X1=" + X[i][1]+ " 의 입력을 인가하면, ");
            System.out.println("두 은닉 뉴론의 출력은 각각 다음과 같다.");
            double w1 = 1/(1 + Math.exp(-(w_ji[0][0] * X[i][0] + w_ji[0][1] * X[i][1] - w_ji[0][2])));
            double w2 = 1/(1 + Math.exp(-(w_ji[1][0] * X[i][0] + w_ji[1][1] * X[i][1] - w_ji[1][2])));
            System.out.printf("%.2f\n", w1);
            System.out.printf("%.2f\n", w2);
            System.out.println("이로부터 도출되는 출력값은 다음과 같다.");
            double d = 1/(1 + Math.exp(-(w_kj[0][0] * w1 + w_kj[0][1] * w2 - w_kj[0][2])));
            System.out.printf("%.2f\n\n", d);

        }
    }
}
